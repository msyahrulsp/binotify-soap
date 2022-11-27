# Binotify SOAP Service

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