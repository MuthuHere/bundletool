/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.tools.build.bundletool.device;

import com.android.tools.build.bundletool.model.exceptions.ParseException;
import com.google.common.collect.ImmutableList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Parses the Package Name from the output of "aapt2 dump badging". */
public final class BadgingPackageNameParser {

  private static final Pattern PACKAGE_NAME_PATTERN = Pattern.compile(".*? name='(.*?)'.*");

  private BadgingPackageNameParser() {}

  public static String parse(ImmutableList<String> badgingOutput) {
    String packageLine =
        badgingOutput.stream()
            .filter(line -> line.trim().startsWith("package:"))
            .findFirst()
            .orElseThrow(
                () ->
                    new ParseException(
                        String.format(
                            "'package:' line not found in badging output\n: %s",
                            String.join("\n", badgingOutput))));
    Matcher matcher = PACKAGE_NAME_PATTERN.matcher(packageLine);
    if (!matcher.matches()) {
      throw new ParseException(String.format("'name=' not found in package line: %s", packageLine));
    }
    return matcher.group(1);
  }
}
