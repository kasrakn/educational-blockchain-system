# some tips to run your code

1. At first, you should install the openssl library by using the library attached to the project

2. In order to prevent some problems with running this code, install this command on your linux machines

<br>

### To install OpenSSL development package on Debian, Ubuntu or their derivatives:

```bash
$ sudo apt-get install libssl-dev
```
### To install OpenSSL development package on Fedora, CentOS or RHEL:
```bash
$ sudo yum install openssl-devel 
```


## How to run the code?
```bash
$ g++ -I/usr/include/openssl/ -Wall my_rsa.c -o my_rsa  -lcrypto -ldl
```