# Binotify SOAP Service
Service yang digunakan oleh binotify rest dan binotify app untuk proses subscription dan lainnya. Terhubung dengan database mysql.
## Skema Basis Data
![image](https://user-images.githubusercontent.com/71055612/205102275-7b281994-1dc4-4310-9481-3d1f4e531452.png)
Terdapat perubahan skema di atas sebagai berikut:
- Penambahan tabel api_key, digunakan untuk verifikasi api key dari binotify php dan binotify rest.  
Atribut:
```
  `id` int NOT NULL AUTO_INCREMENT,
  `services` varchar(255) NOT NULL,
  `api_key` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
```
## Features
### Logging
Digunakan untuk memasukkan setiap request yang diterima SOAP ke database.  
Usage:
1. Add code below in your controller.
```java
@Resource WebServiceContext wsContext;
```
2. Add code below in your interface.
```java
@Resource
```
3. Add code below to log the incoming request.
```java
// void
insertLog(wsContext, "log message", "endpoint");
```

### API Key
Digunakan untuk verifikasi incoming request yang berkaitan dengan subscription.  
Usage:
1. Add code below before any operation in your controller.
```java
// return boolean
verifyAPIKey(wsContext)
```
2. Do step 1 and 2 in logging above (we need WebServiceContext to read http request headers).

Example for API key verification and logging:
```java
public class CheckStatusController extends Database implements CheckStatusInterface {
    @Resource WebServiceContext wsContext; // add to your controller and interface
    @WebMethod
    public boolean getStatus() {
        if (verifyAPIKey(wsContext)) { // verify the incoming request API key
            String query = "select * from subscription where creator_id = " + creator_id + " and subscriber_id = " + subscriber_id;
            String status = "";
            try {
                ResultSet res = this.executeQuery(query);
                List<Map<String, Object>> data = getFormattedRes(res);
                if (data != null) {
                    status = (String) data.get(0).get("status");

                    // add this to log the incoming request
                    insertLog(wsContext, "Mengecek status subscription", "/subscription-status");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            return status.equals("ACCEPTED");
        } else {
            return false;
        }
    }
}
```

## Pembagian Tugas SOAP
| Task        | NIM           |
| ------------- |-------------|
|Request Subscription | 13520080 |
|Check Request | 13520002 |
|Accept/Reject Request | 13520161 |
