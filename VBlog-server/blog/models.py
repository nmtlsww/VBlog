from django.db import models
from django.contrib.auth.models import User
from serializers import BlogSerializer, CommentSerializer, CategorySerializer

# Create your models here.
class Category(models.Model):
	category_name = models.CharField(max_length=20, blank=True)
	create_time = models.DateTimeField(auto_now_add=True)
	def __unicode__(self):
		return u'%s' % (self.category_name)

	@staticmethod
	def to_json_list(category_list):
		return CategorySerializer().to_list_json(category_list)

class Blog(models.Model):
	user = models.ForeignKey(User)
	title = models.CharField(max_length=100)
	content = models.TextField(max_length=5000)
	category = models.ForeignKey(Category)
	create_time = models.DateTimeField(auto_now_add=True)
	update_time = models.DateTimeField(auto_now=True)
	def __unicode__(self):
		return u'%s %s %s' % (self.title, self.user, self.create_time)
	class Meta:
		ordering = ['-create_time']

	def to_json(self):
		return BlogSerializer().to_sign_json(self)
	@staticmethod
	def to_json_list(blog_list, params):
		return BlogSerializer().to_list_json(blog_list, params)

class Comment(models.Model):
	username = models.CharField(max_length=100)
	email = models.EmailField(blank=True, verbose_name='e-mail')
	blog = models.ForeignKey(Blog)
	content = models.TextField(max_length=500)
	create_time = models.DateTimeField(auto_now_add=True)

	def __unicode__(self):
		return u'%s' % (self.content)
	class Meta:
		ordering = ['-create_time']

	def to_json(self):
		return CommentSerializer().to_sign_json(self)
	@staticmethod
	def to_json_list(comment_list):
		return CommentSerializer().to_list_json(comment_list)