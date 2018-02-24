import socket
import json
import MySQLdb


#query= select hum,temp from paramtbl where date>'2018-02-08' and date<'2018-02-30'
HOST, PORT = '', 5000
j=''
listen_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
listen_socket.bind((HOST, PORT))
listen_socket.listen(1)
print 'Serving HTTP on port %s ...' % PORT
while True:
    db = MySQLdb.connect(host="localhost",    # your host, usually localhost
                     user="root",         # your username
                     passwd="anand@mysql123",  # your password
                     db="paramdb")        # name of the data base
    cur = db.cursor()
    client_connection, client_address = listen_socket.accept()
    request = client_connection.recv(1024)
    i=1;
    requestString=''
    while request[-i] != '\n':
        requestString=requestString+ request[-i]
        i=i+1
    requestString=requestString[::-1]
    requestJson=json.loads(requestString)
    print "From:"+requestJson['from']+"To: "+requestJson['to']
    query= "select date,time,hum,temp from paramtbl where date>='"+requestJson['from']+"' and date<='"+requestJson['to']+"'"
    cur.execute(query)
    responseJson="{"
    i=0
    for row in cur.fetchall():
        if(i==0):
            responseJson=responseJson+'"'+str(i)+'":{"date":'+'"'+str(row[0])+'","time":"'+str(row[1])+'","hum":"'+str(row[2])+'","temp":"'+str(row[3])+'"}'
        else: 
            responseJson=responseJson+',"'+str(i)+'":{"date":'+'"'+str(row[0])+'","time":"'+str(row[1])+'","hum":"'+str(row[2])+'","temp":"'+str(row[3])+'"}'
        i=i+1  
    responseJson=responseJson+"}"
    http_response = """\
HTTP/1.1 200 OK

"""+responseJson
    client_connection.sendall(http_response)
    client_connection.close()
    db.close()
 
