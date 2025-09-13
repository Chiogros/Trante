##################
# Global options #
##################
# -dontoptimize
# -dontobfuscate
-dontshrink

# Do not enable this since it's a FOSS project
# and people may kindly send stacktraces in case of bug.
# -repackageclasses

################
# Keep options #
################
# -keepattributes SourceFile,LineNumberTable
# -keepattributes LocalVariableTable, LocalVariableTypeTable

-keepclasseswithmembers class org.apache.sshd.**
-keepclasseswithmembers class org.bouncycastle.**

# Following classes are used by bouncycastle for LDAP.
# Since LDAP isn't expected to be used, we may ignore those
# missing classes.
-dontwarn javax.naming.directory.*
-dontwarn javax.naming.NamingEnumeration
-dontwarn javax.naming.NamingException

-dontwarn java.rmi.**
-dontwarn javax.security.auth.callback.NameCallback
-dontwarn javax.security.auth.login.AppConfigurationEntry$LoginModuleControlFlag
-dontwarn javax.security.auth.login.AppConfigurationEntry
-dontwarn javax.security.auth.login.Configuration
-dontwarn javax.security.auth.login.CredentialException
-dontwarn javax.security.auth.login.FailedLoginException
-dontwarn javax.security.auth.login.LoginContext

-dontwarn org.apache.tomcat.jni.Error
-dontwarn org.apache.tomcat.jni.File
-dontwarn org.apache.tomcat.jni.Local
-dontwarn org.apache.tomcat.jni.Socket

-dontwarn org.bouncycastle.asn1.pkcs.PrivateKeyInfo
-dontwarn org.bouncycastle.crypto.AsymmetricCipherKeyPair
-dontwarn org.bouncycastle.crypto.KeyGenerationParameters
-dontwarn org.bouncycastle.crypto.SecretWithEncapsulation
-dontwarn org.bouncycastle.crypto.params.AsymmetricKeyParameter
-dontwarn org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
-dontwarn org.bouncycastle.crypto.prng.RandomGenerator
-dontwarn org.bouncycastle.crypto.prng.VMPCRandomGenerator
-dontwarn org.bouncycastle.crypto.util.PrivateKeyFactory
-dontwarn org.bouncycastle.crypto.util.PrivateKeyInfoFactory
-dontwarn org.bouncycastle.jcajce.interfaces.EdDSAKey
-dontwarn org.bouncycastle.jcajce.interfaces.EdDSAPrivateKey
-dontwarn org.bouncycastle.jcajce.interfaces.EdDSAPublicKey
-dontwarn org.bouncycastle.jcajce.spec.RawEncodedKeySpec
-dontwarn org.bouncycastle.openssl.PEMDecryptorProvider
-dontwarn org.bouncycastle.openssl.PEMEncryptedKeyPair
-dontwarn org.bouncycastle.openssl.PEMKeyPair
-dontwarn org.bouncycastle.openssl.PEMParser
-dontwarn org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
-dontwarn org.bouncycastle.openssl.jcajce.JcaPEMWriter
-dontwarn org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder
-dontwarn org.bouncycastle.operator.InputDecryptorProvider
-dontwarn org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo
-dontwarn org.bouncycastle.pkcs.PKCSException
-dontwarn org.bouncycastle.pkcs.jcajce.JcePKCSPBEInputDecryptorProviderBuilder
-dontwarn org.bouncycastle.pqc.crypto.mlkem.MLKEMExtractor
-dontwarn org.bouncycastle.pqc.crypto.mlkem.MLKEMGenerator
-dontwarn org.bouncycastle.pqc.crypto.mlkem.MLKEMKeyGenerationParameters
-dontwarn org.bouncycastle.pqc.crypto.mlkem.MLKEMKeyPairGenerator
-dontwarn org.bouncycastle.pqc.crypto.mlkem.MLKEMParameters
-dontwarn org.bouncycastle.pqc.crypto.mlkem.MLKEMPrivateKeyParameters
-dontwarn org.bouncycastle.pqc.crypto.mlkem.MLKEMPublicKeyParameters
-dontwarn org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimeKEMExtractor
-dontwarn org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimeKEMGenerator
-dontwarn org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimeKeyGenerationParameters
-dontwarn org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimeKeyPairGenerator
-dontwarn org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimeParameters
-dontwarn org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimePrivateKeyParameters
-dontwarn org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimePublicKeyParameters

-dontwarn org.ietf.jgss.GSSContext
-dontwarn org.ietf.jgss.GSSCredential
-dontwarn org.ietf.jgss.GSSException
-dontwarn org.ietf.jgss.GSSManager
-dontwarn org.ietf.jgss.GSSName
-dontwarn org.ietf.jgss.MessageProp
-dontwarn org.ietf.jgss.Oid
-dontwarn sun.security.x509.X509Key
