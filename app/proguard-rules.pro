##################
# Global options #
##################
# Enable all optimizations for release builds.
# -dontoptimize
# -dontobfuscate
# -dontshrink

# Do not enable this since it's a FOSS project
# and people may kindly send stacktraces in case of bug.
# -repackageclasses

-keepattributes SourceFile,LineNumberTable
-keepattributes LocalVariableTable, LocalVariableTypeTable

################
# Keep options #
################
# SSH
-keep,allowoptimization,allowobfuscation class org.apache.sshd.common.io.nio2.Nio2ServiceFactoryFactory { *; }
-keep,allowoptimization,allowobfuscation class org.apache.sshd.common.session.helpers.SessionHelper { *; }
-keep,allowoptimization class org.apache.sshd.common.util.security.bouncycastle.BouncyCastleSecurityProviderRegistrar { *; }
-keep,allowoptimization class org.apache.sshd.common.util.security.eddsa.EdDSASecurityProviderRegistrar { *; }
-keep,allowoptimization class org.apache.sshd.common.util.security.SunJCESecurityProviderRegistrar { *; }
-dontwarn org.apache.sshd.**