from flask import Flask, request
import pickle
app = Flask(__name__)
 
@app.route("/", methods=['Post'])
def hello():
    x = request.form.get('message')
    print(x)
    filehandler = open("Fruits.obj","wb")
    pickle.dump(x,filehandler)
    filehandler.close()
    return "Hello World!"
 
if __name__ == "__main__":
    app.run(ssl_context=('cert.pem', 'key.pem'))

#C:\ProgramData\Oracle\Java\javapath