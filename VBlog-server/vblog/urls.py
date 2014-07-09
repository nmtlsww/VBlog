from django.conf.urls import patterns, include, url
from django.contrib import admin
from blog import views 
import os
from blog.models import Blog

admin.autodiscover()

urlpatterns = patterns('',

    url(r'^blog/list/$', views.blog_list),
    url(r'^blog/(\d*)/$', views.get_blog),
    url(r'^add/blog/$', views.add_blog),
    url(r'^del/blog/(\d*)/$', views.del_blog),
    url(r'^update/blog/$', views.update_blog),
    url(r'^add/comment/$', views.add_comment),
    url(r'^del/comment/(\d*)/$', views.del_comment),
    url(r'^update/comment/$', views.update_comment),
    url(r'^comment/list/(\d*)/$', views.comment_list),
    url(r'^add/category/$', views.add_category),
    url(r'^del/category/(\d*)/$', views.del_category),
    url(r'^category/list/$', views.category_list),
    url(r'^vblog-admin/', include(admin.site.urls)),
    url(r'^static/(?P<path>.*)$', 'django.views.static.serve',{'document_root': os.path.join(os.path.dirname(__file__), '../static').replace('\\','/')}),
    url(r'^login/$', views.login),
    url(r'^islogin/$', views.is_login),
    url(r'^logout/$', views.logout),
    url(r'^$', views.index),
)
