package necklace;


import android.util.Log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ZZB on 2018/5/8.
 */
public class NeckLaceRxUtils {
    public static void dispose(@Nullable Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static Consumer<? super Throwable> errorConsumer(@Nonnull final String tag) {
        return errorConsumer(tag, null);
    }

    public static Consumer<? super Throwable> errorConsumer(
            @Nonnull final String tag,
            @Nullable final String msg) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(tag, msg, throwable);
            }
        };
    }
}
