import json
from django.core.serializers.json import DjangoJSONEncoder

def success():
	dic = {}
	dic['status'] = 'success'
	return json.dumps(dic, cls=DjangoJSONEncoder)

def fail(error, islogin=None):
	dic = {}
	dic['status'] = 'fail'
	dic['error'] = error
	if islogin != None:
		dic['islogin'] = islogin
	return json.dumps(dic, cls=DjangoJSONEncoder)