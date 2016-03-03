/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.vcs.log.util;

import com.intellij.openapi.util.Pair;
import com.intellij.vcs.log.VcsUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VcsUserUtil {
  @NotNull private static final Pattern NAME_WITH_DOT = Pattern.compile("(\\w*)\\.(\\w*)");
  @NotNull private static final Pattern NAME_WITH_SPACE = Pattern.compile("(\\w*) (\\w*)");

  @NotNull
  public static String toExactString(@NotNull VcsUser user) {
    return getString(user.getName(), user.getEmail());
  }

  @NotNull
  private static String getString(@NotNull String name, @NotNull String email) {
    if (name.isEmpty()) return email;
    if (email.isEmpty()) return name;
    return name + " <" + email + ">";
  }

  public static boolean isSamePerson(@NotNull VcsUser user1, @NotNull VcsUser user2) {
    return getNameInStandardForm(getName(user1)).equals(getNameInStandardForm(getName(user2)));
  }

  @NotNull
  public static String getShortPresentation(@NotNull VcsUser user) {
    return getName(user);
  }

  @NotNull
  private static String getName(@NotNull VcsUser user) {
    if (!user.getName().isEmpty()) return user.getName();
    String emailNamePart = getNameFromEmail(user.getEmail());
    if (emailNamePart != null) return emailNamePart;
    return user.getEmail();
  }

  @Nullable
  public static String getNameFromEmail(@NotNull String email) {
    int at = email.indexOf('@');
    String emailNamePart = null;
    if (at > 0) {
      emailNamePart = email.substring(0, at);
    }
    return emailNamePart;
  }

  @NotNull
  public static String getNameInStandardForm(@NotNull String name) {
    Pair<String, String> firstAndLastName = getFirstAndLastName(name);
    if (firstAndLastName != null) {
      return firstAndLastName.first.toLowerCase() + " " + firstAndLastName.second.toLowerCase();
    }
    return name.toLowerCase();
  }

  @Nullable
  public static Pair<String, String> getFirstAndLastName(@NotNull String name) {
    Matcher nameWithDotMatcher = NAME_WITH_DOT.matcher(name);
    if (nameWithDotMatcher.matches()) {
      return Pair.create(nameWithDotMatcher.group(1), nameWithDotMatcher.group(2));
    }
    Matcher nameWithSpaceMatcher = NAME_WITH_SPACE.matcher(name);
    if (nameWithSpaceMatcher.matches()) {
      return Pair.create(nameWithSpaceMatcher.group(1), nameWithSpaceMatcher.group(2));
    }
    return null;
  }
}
