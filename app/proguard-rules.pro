# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn javax.management.MBeanException
-dontwarn javax.management.ReflectionException
-dontwarn net.i2p.crypto.eddsa.EdDSAPublicKey
-dontwarn org.bouncycastle.crypto.prng.RandomGenerator
-dontwarn org.bouncycastle.crypto.prng.VMPCRandomGenerator
-dontwarn org.bouncycastle.pqc.crypto.mlkem.MLKEMParameters
-dontwarn org.bouncycastle.pqc.crypto.ntruprime.SNTRUPrimeParameters
-dontwarn org.slf4j.impl.StaticLoggerBinder