package com.sensorfields.livingscreen.android.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sensorfields.livingscreen.android.await
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SignInWithGoogleUseCase @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    suspend operator fun invoke(idToken: String): Either<Error, Unit> {
        return try {
            firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .await()
            Unit.right()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            Error.General.left()
        }
    }

    sealed class Error {
        object General : Error()
    }
}