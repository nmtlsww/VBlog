from django.http import HttpResponse
from blog import response_json

def authenticated(method):
    def wrapper(request, *args):
        if not request.user.is_authenticated():
            return HttpResponse(response_json.fail('not login', islogin=False))
        return method(request, *args)
    return wrapper