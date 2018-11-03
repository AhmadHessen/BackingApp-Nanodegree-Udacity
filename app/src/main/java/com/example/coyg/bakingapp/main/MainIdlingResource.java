package com.example.coyg.bakingapp.main;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainIdlingResource implements IdlingResource
{
    @Nullable
    private volatile ResourceCallback resourceCallback;

    private AtomicBoolean atomicBoolean = new AtomicBoolean (true);

    @Override
    public String getName()
    {
        return this.getClass ().getName ();
    }

    @Override
    public boolean isIdleNow()
    {
        return atomicBoolean.get ();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback)
    {
        this.resourceCallback = callback;
    }

    public void setIdle(boolean idle)
    {
        atomicBoolean.set (idle);

        if(idle && resourceCallback != null)
            resourceCallback.onTransitionToIdle ();
    }
}
