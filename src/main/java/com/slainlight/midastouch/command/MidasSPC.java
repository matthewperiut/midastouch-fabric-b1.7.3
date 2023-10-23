package com.slainlight.midastouch.command;

import com.matthewperiut.spc.api.CommandRegistry;

public class MidasSPC
{
    public static void add()
    {
        CommandRegistry.add(new GoldenCommand());
    }
}
