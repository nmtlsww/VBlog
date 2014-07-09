# -*- coding: UTF-8 -*-

from django.contrib.auth.models import User
import json
from django.core.serializers.json import DjangoJSONEncoder

class BlogSerializer():

	def to_sign_json(self, blog):
		blog_dic = {}
		blog_dic['id'] = blog.id
		blog_dic['user_id'] = blog.user.id
		blog_dic['username'] = blog.user.username
		blog_dic['title'] = blog.title
		blog_dic['content'] = blog.content
		blog_dic['category_id'] = blog.category.id
		blog_dic['category_name'] = blog.category.category_name
		blog_dic['create_time'] = blog.create_time
		blog_dic['update_time'] = blog.update_time

		return json.dumps(blog_dic, cls=DjangoJSONEncoder)

	def to_list_json(self, list, params):
		blog_list = []
		for blog in list:
			blog_dic = {}
			blog_dic['id'] = blog.id
			blog_dic['user_id'] = blog.user.id
			blog_dic['username'] = blog.user.username
			blog_dic['title'] = blog.title
			if len(blog.content) > 200:
				blog_dic['content'] = blog.content[0:200] + '...'
			else:
				blog_dic['content'] = blog.content
			blog_dic['category_id'] = blog.category.id
			blog_dic['category_name'] = blog.category.category_name
			blog_dic['create_time'] = blog.create_time
			blog_dic['update_time'] = blog.update_time
			blog_list.append(blog_dic)

		blog_list_dic = {}
		blog_list_dic['blog_list'] = blog_list
		blog_list_dic['begin'] = params['begin']
		blog_list_dic['end'] = params['end']
		blog_list_dic['total'] = params['total']

		return json.dumps(blog_list_dic, cls=DjangoJSONEncoder)

class CommentSerializer():

	def to_sign_json(self, comment):
		comment_dic = {}
		comment_dic['id'] = comment.id
		comment_dic['blog_id'] = comment.blog.id
		comment_dic['username'] = comment.username
		comment_dic['content'] = comment.content
		comment_dic['create_time'] = comment.create_time
		comment_dic['email'] = comment.email

		return json.dumps(comment_dic, cls=DjangoJSONEncoder)

	def to_list_json(self, list):
		comment_list = []
		for comment in list:
			comment_dic = {}
			comment_dic['id'] = comment.id
			comment_dic['username'] = comment.username
			comment_dic['email'] = comment.email
			comment_dic['content'] = comment.content
			comment_dic['create_time'] = comment.create_time
			comment_list.append(comment_dic)

		comment_list_dic = {}
		comment_list_dic['comment_list'] = comment_list

		return json.dumps(comment_list_dic, cls=DjangoJSONEncoder)

class CategorySerializer():

	def to_list_json(self, list):
		category_list = []
		for category in list:
			category_dic = {}
			category_dic['id'] = category.id
			category_dic['category_name'] = category.category_name
			category_dic['create_time'] = category.create_time
			category_list.append(category_dic)

		category_list_dic = {}
		category_list_dic['category_list'] = category_list

		return json.dumps(category_list_dic, cls=DjangoJSONEncoder)

class UserSerializer():

	def to_sign_json(self, user):
		user_dic = {}
		user_dic['id'] = user.id
		user_dic['username'] = user.username
		user_dic['email'] = user.email
		user_dic['first_name'] = user.first_name
		user_dic['last_name'] = user.last_name

		return json.dumps(user_dic, cls=DjangoJSONEncoder)