package com.slainlight.midastouch.command;

import com.matthewperiut.retrocommands.api.CommandRegistry;

public class MidasRetroCommands
{
    public static void add()
    {
        CommandRegistry.add(new GoldenCommand());
    }
}
