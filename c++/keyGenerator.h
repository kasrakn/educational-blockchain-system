#include <stdio.h>
#include <openssl/rsa.h>
#include <openssl/pem.h>
#include <string>

using namespace std;

bool generate_key(int id)
{
	int				ret = 0;
	RSA				*r = NULL;
	BIGNUM			*bne = NULL;
	BIO				*bp_public = NULL, *bp_private = NULL;
    string           privateKeyPath;
    string           publicKeyPath;
    const char      *prkp;
    const char      *pukp;

	int				bits = 2048;
	unsigned long	e = RSA_F4;

	// 1. generate rsa key
	bne = BN_new();
	ret = BN_set_word(bne,e);
	if(ret != 1){
		goto free_all;
	}

	r = RSA_new();
	ret = RSA_generate_key_ex(r, bits, bne, NULL);
	if(ret != 1){   
		goto free_all;
	}

	// 2. save public key
    publicKeyPath = "keys/publicKey/" + std::to_string(id) + ".pem";
    pukp = publicKeyPath.c_str();
	bp_public = BIO_new_file(pukp, "w+");
	ret = PEM_write_bio_RSAPublicKey(bp_public, r);
	if(ret != 1){
		goto free_all;
	}

	// 3. save private key
    privateKeyPath = "keys/privateKey/" + std::to_string(id) + ".pem";
    prkp = privateKeyPath.c_str();
	bp_private = BIO_new_file(prkp, "w+");
	ret = PEM_write_bio_RSAPrivateKey(bp_private, r, NULL, NULL, 0, NULL, NULL);

	// 4. free
free_all:

	BIO_free_all(bp_public);
	BIO_free_all(bp_private);
	RSA_free(r);
	BN_free(bne);

	return (ret == 1);
}