# Binotify SOAP Service

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
insertLog(wsContext, "request", "endpoint");
```

### API Key
Digunakan untuk verifikasi incoming request yang berkaitan dengan subscription.  
Usage:
1. Add code below before any operation in your controller.
```java
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

## Endpoints
### Generate API Key
Digunakan untuk mendapatkan API key dari user yang melakukan registrasi di binotify premium. Menyimpan api-key dan email user ke database. Dipanggil melalui binotify REST.  

@param {string} email  
@return {string} api-key
```
POST /generate-api-key?wsdl
```
Request
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:int="http://interfaces.binotify.com/">
    <soapenv:Header/>
    <soapenv:Body>
        <int:generateAPIKey>
            <email>user@gmail.com</email>
        </int:generateAPIKey>
    </soapenv:Body>
</soapenv:Envelope>
```
Response
```xml
<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
<S:Body>
    <ns2:generateAPIKeyResponse xmlns:ns2="http://interfaces.binotify.com/">
        <return>LcS0-SmJjjUoooMAKOANu_JdFij7AOb1kaFkNXuGVWY</return>
    </ns2:generateAPIKeyResponse>
</S:Body>
</S:Envelope>
```