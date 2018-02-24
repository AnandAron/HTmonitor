import requests
import MySQLdb
import time
ipadd="169.254.67.11"
db_username="root"
db_pwd="anand@mysql123"
db_name="paramdb"
while(1):
  req=requests.get("http://"+ipadd+"/")
  res= req.content
  print res[100:104]
  print res[128:130]
  temps=res[100:104]
  hums=res[128:130]
  temp=float(res[100:104])
  if (res[128:130]!=': '):
    hum=int(res[128:130])
  else: hum=999
  if(temp<60 and temp>0 and hum<60 and hum>30):
    db=MySQLdb.connect("localhost",db_username,db_pwd,db_name)
    cursor=db.cursor()
    query="insert into paramtbl (date,time,hum,temp) values ( NOW(),NOW(),"+hums+","+temps+")"
    try:
      cursor.execute(query)
      db.commit()
    except:
      db.rollback()
    print "Updated!!"
    db.close()
    time.sleep(300)
