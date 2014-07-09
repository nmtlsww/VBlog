# Create your views here.
from blog.models import Blog, Category, Comment
from blog.serializers import BlogSerializer
from django.http import HttpResponse
from django.core import serializers
from django.contrib.auth.models import User
from django.contrib import auth
from blog import response_json
import traceback
from blog.decorators import authenticated
from serializers import UserSerializer
from django.shortcuts import render_to_response

def index(request):
	return render_to_response('index.html')

@authenticated
def add_blog(request):
	try:
		req = convertRequest(request)

	 	category_name = req.get('category_name', '')
	 	if category_name != '':
	 		category_name = req.get('category_name', '')
	 		category = Category.objects.get_or_create(category_name=category_name)[0]
			category_id = category.id
		else:
			category_id = req.get('category_id', 0)

		title = req.get('title', '')
		content =req.get('content', '')
		user_id = req.get('user_id', 0)

		if title != '' and category_id != 0 and user_id != 0:
			user = User.objects.get(id=user_id)
			category = Category.objects.get(id=category_id)
			blog = Blog(title=title, content=content, category=category, user=user)
			blog.save()
			return HttpResponse(response_json.success())

	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

@authenticated
def del_blog(request, blog_id):
	try:
		blog = Blog.objects.get(id=blog_id)
		blog.delete()
		return HttpResponse(response_json.success())
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

@authenticated
def update_blog(request):
	try:
		req = convertRequest(request)

	 	id = req['blog_id']

	 	category_name = req.get('category_name', '')
	 	if category_name != '':
	 		category_name = req.get('category_name', '')
	 		category = Category.objects.get_or_create(category_name=category_name)[0]
			category_id = category.id
		else:
			category_id = req.get('category_id', 0)

		blog = Blog.objects.get(id=id)
		title = req.get('title', blog.title)
		content = req.get('content', blog.content)
		if category_id == 0 or category_id == blog.category.id:
			category = blog.category
		else:
			category = Category.objects.get(id=category_id)

		user_id = req.get('user_id', 0)
		if user_id == 0 or user_id == blog.user.id:
			user = blog.user
		else:
			user = User.objects.get(id=user_id)
		# Looking forward to a better solution
		Blog.objects.filter(id=id).update(title=title, content=content, category=category, user=user)

		return HttpResponse(response_json.success())
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

def blog_list(request):
	try:
		req = convertRequest(request)

	 	category_id = req.get('category_id', 0)

	 	if category_id == 0:
	 		blog_list = Blog.objects.all()
	 	else:
	 		blog_list = Blog.objects.filter(category_id=category_id)

	 	total = len(blog_list)
	 	begin = int(req.get('begin', 0))
	 	if begin >= total and total > 0:
	 		return HttpResponse(response_json.fail('begin > data total'))
	 	num = int(req.get('num', 5))
	 	end = begin + num
	 	if end > total:
	 		end = total

		data = Blog.to_json_list(blog_list[begin:end], {'begin':begin, 'end':end, 'total':total})
		return HttpResponse(data, content_type="application/json")
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

def get_blog(request, id):
	try:
		blog = Blog.objects.get(id=id)
		data = blog.to_json()
		return HttpResponse(data, content_type="application/json")
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

def add_comment(request):
	try:
		req = convertRequest(request)

		content = req.get('content', '')
		blog_id = req.get('blog_id', 0)
		email = req.get('email', '')
		username = req.get('username', '')

		if blog_id != 0 and username != '':
			blog = Blog.objects.get(id=blog_id)
			comment = Comment(content=content, blog=blog, username=username, email=email)
			comment.save()
			return HttpResponse(response_json.success())
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

@authenticated
def del_comment(request, comment_id):
	try:
		comment = Comment.objects.get(id=comment_id)
		comment.delete()
		return HttpResponse(response_json.success())
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

@authenticated
def update_comment(request):
	try:
		req = convertRequest(request)
		id = req['comment_id']

		# get the single object 
		comment = Comment.objects.get(id=id)
		content = req.get('content', comment.content)
		username = req.get('username', comment.username)
		email = req.get('email', comment.email)
		blog_id = req.get('blog_id', 0)
		if blog_id == 0 or blog_id == comment.blog.id:
			blog = comment.blog
		else:
			blog = Blog.objects.get(id=blog_id)

		# Looking forward to a better solution
		Comment.objects.filter(id=id).update(content=content, blog=blog, username=username, email=email)
		return HttpResponse(response_json.success())
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

def comment_list(request, blog_id):
	try:
		comment_list = Comment.objects.filter(blog=blog_id)
		return HttpResponse(Comment.to_json_list(comment_list), content_type="application/json")
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

@authenticated
def add_category(request):
	try:
		req = convertRequest(request)

		category_name = req['category_name']
		category = Category(category_name=category_name)
		category.save()
		return HttpResponse(response_json.success())
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

@authenticated
def del_category(request, category_id):
	try:
		category = Category.objects.get(id=category_id)
		category.delete()
		return HttpResponse(response_json.success())
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

def category_list(request):
	try:
		category_list = Category.objects.all()
		print request.session.session_key
		return HttpResponse(Category.to_json_list(category_list), content_type="application/json")
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

def login(request):
	try:
		req = convertRequest(request)

		username = req.get('username', '')
		password = req.get('password', '')
		user = auth.authenticate(username=username, password=password)
		if user is not None and user.is_active:
			auth.login(request, user)
			return HttpResponse(UserSerializer().to_sign_json(user))
	except Exception, e:
		return HttpResponse(response_json.fail(traceback.format_exc()))
	return HttpResponse(response_json.fail(''))

def is_login(request):
	if request.user.is_authenticated():
		return HttpResponse(response_json.success())
	else:
		return HttpResponse(response_json.fail(''))

def logout(request):
	if request.user.is_authenticated():
		auth.logout(request)
		return HttpResponse(response_json.success())
	else:
		return HttpResponse(response_json.fail(''))

def convertRequest(request):
	req = {}
	if request.method == 'GET':
		req = request.GET
	elif request.method == 'POST':
		req = request.POST
	return req

