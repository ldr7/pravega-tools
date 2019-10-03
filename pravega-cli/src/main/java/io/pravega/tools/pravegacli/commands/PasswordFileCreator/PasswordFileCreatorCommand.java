/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package io.pravega.tools.pravegacli.commands.PasswordFileCreator;

import com.google.common.base.Strings;
import io.pravega.tools.pravegacli.commands.CommandArgs;
import lombok.Cleanup;
import io.pravega.tools.pravegacli.commands.Command;
import io.pravega.controller.server.rpc.auth.StrongPasswordProcessor;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordFileCreatorCommand extends Command {
    static final String COMPONENT = "PasswordFileCreator";
    public PasswordFileCreatorCommand(CommandArgs args){super(args);}
    @Override
    public void execute() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        ensureArgCount(2);

        String fileName = getCommandArgs().getArgs().get(0);
        String s = getCommandArgs().getArgs().get(1);
        StrongPasswordProcessor passwordEncryptor = StrongPasswordProcessor.builder().build();
        try (FileWriter writer = new FileWriter(fileName))
        {
            while (true) {
                if (Strings.isNullOrEmpty(s)) {
                    break;
                }
                String[] lists = s.split(":");
                String toWrite = lists[0] + ":" + passwordEncryptor.encryptPassword(lists[1])+ ":" + lists[2];
                writer.write(toWrite + "\n");
                writer.flush();
            }
        }
    }


    public static CommandDescriptor descriptor() {
        return new CommandDescriptor(COMPONENT, "create-password-file", "Generates file with password according to filename and credential given as argument.");
    }
}

