/*
 * Copyright (C) 2014 The Android Open Source Project
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
 * limitations under the License.
 */

package com.android.inputmethod.latin;

import android.os.Build;
import android.util.Log;
import android.view.inputmethod.InputMethodSubtype;

import com.android.inputmethod.compat.BuildCompatUtils;
import com.android.inputmethod.compat.InputMethodSubtypeCompatUtils;
import com.android.inputmethod.latin.common.Constants;
import com.android.inputmethod.latin.common.LocaleUtils;
import com.android.inputmethod.latin.utils.SubtypeLocaleUtils;

import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.android.inputmethod.latin.common.Constants.Subtype.KEYBOARD_MODE;

/**
 * Enrichment class for InputMethodSubtype to enable concurrent multi-lingual input.
 *
 * Right now, this returns the extra value of its primary subtype.
 */
// non final for easy mocking.
public class RichInputMethodSubtype {
    private static final String TAG = RichInputMethodSubtype.class.getSimpleName();

    private static final HashMap<Locale, Locale> sLocaleMap = initializeLocaleMap();

    private static final HashMap<Locale, Locale> initializeLocaleMap() {
        final HashMap<Locale, Locale> map = new HashMap<>();
        if (BuildCompatUtils.EFFECTIVE_SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Locale#forLanguageTag is available on API Level 21+.
            // TODO: Remove this workaround once when we become able to deal with "sr-Latn".
            map.put(Locale.forLanguageTag("sr-Latn"), new Locale("sr_ZZ"));
        }
        return map;
    }

    @Nonnull
    private final InputMethodSubtype mSubtype;
    @Nonnull
    private final Locale mLocale;
    @Nonnull
    private final Locale mOriginalLocale;

    public RichInputMethodSubtype(@Nonnull final InputMethodSubtype subtype) {
        mSubtype = subtype;
        mOriginalLocale = InputMethodSubtypeCompatUtils.getLocaleObject(mSubtype);
        final Locale mappedLocale = sLocaleMap.get(mOriginalLocale);
        mLocale = mappedLocale != null ? mappedLocale : mOriginalLocale;
    }


    // Extra values are determined by the primary subtype. This is probably right, but
    // we may have to revisit this later.
    public String getExtraValueOf(@Nonnull final String key) {
        return mSubtype.getExtraValueOf(key);
    }

    // The mode is also determined by the primary subtype.
    public String getMode() {
        return mSubtype.getMode();
    }

    public boolean isNoLanguage() {
        return SubtypeLocaleUtils.NO_LANGUAGE.equals(mSubtype.getLocale());
    }

    public String getNameForLogging() {
        return toString();
    }

    // InputMethodSubtype's display name for spacebar text in its locale.
    //        isAdditionalSubtype (T=true, F=false)
    // locale layout  |  Middle      Full
    // ------ ------- - --------- ----------------------
    //  en_US qwerty  F  English   English (US)           exception
    //  en_GB qwerty  F  English   English (UK)           exception
    //  es_US spanish F  Español   Español (EE.UU.)       exception
    //  fr    azerty  F  Français  Français
    //  fr_CA qwerty  F  Français  Français (Canada)
    //  fr_CH swiss   F  Français  Français (Suisse)
    //  de    qwertz  F  Deutsch   Deutsch
    //  de_CH swiss   T  Deutsch   Deutsch (Schweiz)
    //  zz    qwerty  F  QWERTY    QWERTY
    //  fr    qwertz  T  Français  Français
    //  de    qwerty  T  Deutsch   Deutsch
    //  en_US azerty  T  English   English (US)
    //  zz    azerty  T  AZERTY    AZERTY
    // Get the RichInputMethodSubtype's full display name in its locale.
    @Nonnull
    public String getFullDisplayName() {
        if (isNoLanguage()) {
            return SubtypeLocaleUtils.getKeyboardLayoutSetDisplayName(mSubtype);
        }
        return SubtypeLocaleUtils.getSubtypeLocaleDisplayName(mSubtype.getLocale());
    }

    // Get the RichInputMethodSubtype's middle display name in its locale.
    @Nonnull
    public String getMiddleDisplayName() {
        if (isNoLanguage()) {
            return SubtypeLocaleUtils.getKeyboardLayoutSetDisplayName(mSubtype);
        }
        return SubtypeLocaleUtils.getSubtypeLanguageDisplayName(mSubtype.getLocale());
    }

    // Dummy no language QWERTY subtype. See {@link R.xml.method}.
    private static final int SUBTYPE_ID_OF_ENGLISH_LANGUAGE_SUBTYPE = 0xc9194f98;

    @Override
    public int hashCode() {
        return mSubtype.hashCode() + mLocale.hashCode();
    }

    @Override
    public String toString() {
        return "Multi-lingual subtype: " + mSubtype + ", " + mLocale;
    }

    @Nonnull
    public Locale getLocale() {
        return mLocale;
    }

    @Nonnull
    public Locale getOriginalLocale() {
        return mOriginalLocale;
    }

    public boolean isRtlSubtype() {
        // The subtype is considered RTL if the language of the main subtype is RTL.
        return LocaleUtils.isRtlLanguage(mLocale);
    }

    private static final String EXTRA_VALUE_OF_ENGLISH_LANGUAGE_SUBTYPE =
            "KeyboardLayoutSet=" + SubtypeLocaleUtils.QWERTY
                    + "," + Constants.Subtype.ExtraValue.ASCII_CAPABLE
                    + "," + Constants.Subtype.ExtraValue.ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE
                    + "," + Constants.Subtype.ExtraValue.EMOJI_CAPABLE;

    @Nonnull
    public String getKeyboardLayoutSetName() {
        return SubtypeLocaleUtils.getKeyboardLayoutSetName(mSubtype);
    }

    public static RichInputMethodSubtype getRichInputMethodSubtype(
            @Nullable final InputMethodSubtype subtype) {
        if (subtype == null) {
            return getNoLanguageSubtype();
        } else {
            return new RichInputMethodSubtype(subtype);
        }
    }

    @Nonnull
    private static final RichInputMethodSubtype ENGLISH_LANGUAGE_SUBTYPE =
            new RichInputMethodSubtype(InputMethodSubtypeCompatUtils.newInputMethodSubtype(
                    R.string.subtype_en_US, R.drawable.ic_ime_switcher_dark,
                    "en_US", "keyboard",
                    EXTRA_VALUE_OF_ENGLISH_LANGUAGE_SUBTYPE,
                    false /* isAuxiliary */, false /* overridesImplicitlyEnabledSubtype */,
                    SUBTYPE_ID_OF_ENGLISH_LANGUAGE_SUBTYPE));
    // Dummy no language QWERTY subtype. See {@link R.xml.method}.
    private static final int SUBTYPE_ID_OF_NATIVE_LANGUAGE_SUBTYPE = 0x39753b7f;
    private static final String EXTRA_VALUE_OF_NATIVE_LANGUAGE_SUBTYPE =
            "KeyboardLayoutSet=" + "urdu,EmojiCapable"
                    + "," + Constants.Subtype.ExtraValue.ASCII_CAPABLE
                    + "," + Constants.Subtype.ExtraValue.ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE
                    + "," + Constants.Subtype.ExtraValue.EMOJI_CAPABLE;
    @Nonnull
    private static final RichInputMethodSubtype NATIVE_LANGUAGE_SUBTYPE =
            new RichInputMethodSubtype(InputMethodSubtypeCompatUtils.newInputMethodSubtype(
                    R.string.subtype_generic, R.drawable.ic_ime_switcher_dark,
                    "ur", "keyboard",
                    EXTRA_VALUE_OF_NATIVE_LANGUAGE_SUBTYPE,
                    false /* isAuxiliary */, false /* overridesImplicitlyEnabledSubtype */,
                    SUBTYPE_ID_OF_NATIVE_LANGUAGE_SUBTYPE));
    private static final String EXTRA_VALUE_OF_DUMMY_NO_LANGUAGE_SUBTYPE =
            "KeyboardLayoutSet=" + SubtypeLocaleUtils.QWERTY
                    + "," + Constants.Subtype.ExtraValue.ASCII_CAPABLE
                    + "," + Constants.Subtype.ExtraValue.ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE
                    + "," + Constants.Subtype.ExtraValue.EMOJI_CAPABLE;
    private static final String EXTRA_VALUE_OF_DUMMY_EMOJI_SUBTYPE =
            "KeyboardLayoutSet=" + SubtypeLocaleUtils.EMOJI
                    + "," + Constants.Subtype.ExtraValue.EMOJI_CAPABLE;

    // Dummy no language QWERTY subtype. See {@link R.xml.method}.
    private static final int SUBTYPE_ID_OF_DUMMY_NO_LANGUAGE_SUBTYPE = 0xdde0bfd3;
    private static RichInputMethodSubtype englishLanguageSubtype;
    @Nonnull
    private static final RichInputMethodSubtype DUMMY_NO_LANGUAGE_SUBTYPE =
            new RichInputMethodSubtype(InputMethodSubtypeCompatUtils.newInputMethodSubtype(
                    R.string.subtype_no_language_qwerty, R.drawable.ic_ime_switcher_dark,
                    SubtypeLocaleUtils.NO_LANGUAGE, KEYBOARD_MODE,
                    EXTRA_VALUE_OF_DUMMY_NO_LANGUAGE_SUBTYPE,
                    false /* isAuxiliary */, false /* overridesImplicitlyEnabledSubtype */,
                    SUBTYPE_ID_OF_DUMMY_NO_LANGUAGE_SUBTYPE));
    // Caveat: We probably should remove this when we add an Emoji subtype in {@link R.xml.method}.
    // Dummy Emoji subtype. See {@link R.xml.method}.
    private static final int SUBTYPE_ID_OF_DUMMY_EMOJI_SUBTYPE = 0xd78b2ed0;
    private static RichInputMethodSubtype NativeLanguageSubtype;
    @Nonnull
    private static final RichInputMethodSubtype DUMMY_EMOJI_SUBTYPE = new RichInputMethodSubtype(
            InputMethodSubtypeCompatUtils.newInputMethodSubtype(
                    R.string.subtype_emoji, R.drawable.ic_ime_switcher_dark,
                    SubtypeLocaleUtils.NO_LANGUAGE, KEYBOARD_MODE,
                    EXTRA_VALUE_OF_DUMMY_EMOJI_SUBTYPE,
                    false /* isAuxiliary */, false /* overridesImplicitlyEnabledSubtype */,
                    SUBTYPE_ID_OF_DUMMY_EMOJI_SUBTYPE));
    private static RichInputMethodSubtype sNoLanguageSubtype;

    @Nonnull
    public static RichInputMethodSubtype getEnglishLanguageSubtype() {
        RichInputMethodSubtype englishLanguageSubtype = RichInputMethodSubtype.englishLanguageSubtype;
        if (englishLanguageSubtype == null) {
            final InputMethodSubtype rawNoLanguageSubtype = RichInputMethodManager.getInstance()
                    .findSubtypeByLocaleAndKeyboardLayoutSet(
                            "en_US", SubtypeLocaleUtils.QWERTY);
            if (rawNoLanguageSubtype != null) {
                englishLanguageSubtype = new RichInputMethodSubtype(rawNoLanguageSubtype);
            }
        }
        if (englishLanguageSubtype != null) {
            RichInputMethodSubtype.englishLanguageSubtype = englishLanguageSubtype;
            return englishLanguageSubtype;
        }
        Log.w(TAG, "Can't find any language with QWERTY subtype");
        Log.w(TAG, "No input method subtype found; returning dummy subtype: "
                + ENGLISH_LANGUAGE_SUBTYPE);
        return ENGLISH_LANGUAGE_SUBTYPE;
    }

    @Nonnull
    public static RichInputMethodSubtype getNativeLanguageSubtype() {
        RichInputMethodSubtype nativeLanguageSubtype = NativeLanguageSubtype;
        if (nativeLanguageSubtype == null) {
            final InputMethodSubtype rawNoLanguageSubtype = RichInputMethodManager.getInstance()
                    .findSubtypeByLocaleAndKeyboardLayoutSet(
                            "ur", SubtypeLocaleUtils.QWERTY);
            if (rawNoLanguageSubtype != null) {
                nativeLanguageSubtype = new RichInputMethodSubtype(rawNoLanguageSubtype);
            }
        }
        if (nativeLanguageSubtype != null) {
            NativeLanguageSubtype = nativeLanguageSubtype;
            return nativeLanguageSubtype;
        }
        Log.w(TAG, "Can't find any language with QWERTY subtype");
        Log.w(TAG, "No input method subtype found; returning dummy subtype: "
                + NATIVE_LANGUAGE_SUBTYPE);
        return NATIVE_LANGUAGE_SUBTYPE;
    }

    private static RichInputMethodSubtype sEmojiSubtype;

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof RichInputMethodSubtype)) {
            return false;
        }
        final RichInputMethodSubtype other = (RichInputMethodSubtype) o;
        return mSubtype.equals(other.mSubtype) && mLocale.equals(other.mLocale);
    }

    // TODO: remove this method
    @Nonnull
    public InputMethodSubtype getRawSubtype() {
        return mSubtype;
    }

    @Nonnull
    public static RichInputMethodSubtype getNoLanguageSubtype() {
        RichInputMethodSubtype noLanguageSubtype = sNoLanguageSubtype;
        if (noLanguageSubtype == null) {
            final InputMethodSubtype rawNoLanguageSubtype = RichInputMethodManager.getInstance()
                    .findSubtypeByLocaleAndKeyboardLayoutSet(
                            SubtypeLocaleUtils.NO_LANGUAGE, SubtypeLocaleUtils.QWERTY);
            if (rawNoLanguageSubtype != null) {
                noLanguageSubtype = new RichInputMethodSubtype(rawNoLanguageSubtype);
            }
        }
        if (noLanguageSubtype != null) {
            sNoLanguageSubtype = noLanguageSubtype;
            return noLanguageSubtype;
        }
        Log.w(TAG, "Can't find any language with QWERTY subtype");
        Log.w(TAG, "No input method subtype found; returning dummy subtype: "
                + DUMMY_NO_LANGUAGE_SUBTYPE);
        return DUMMY_NO_LANGUAGE_SUBTYPE;
    }

    @Nonnull
    public static RichInputMethodSubtype getEmojiSubtype() {
        RichInputMethodSubtype emojiSubtype = sEmojiSubtype;
        if (emojiSubtype == null) {
            final InputMethodSubtype rawEmojiSubtype = RichInputMethodManager.getInstance()
                    .findSubtypeByLocaleAndKeyboardLayoutSet(
                            SubtypeLocaleUtils.NO_LANGUAGE, SubtypeLocaleUtils.EMOJI);
            if (rawEmojiSubtype != null) {
                emojiSubtype = new RichInputMethodSubtype(rawEmojiSubtype);
            }
        }
        if (emojiSubtype != null) {
            sEmojiSubtype = emojiSubtype;
            return emojiSubtype;
        }
        Log.w(TAG, "Can't find emoji subtype");
        Log.w(TAG, "No input method subtype found; returning dummy subtype: "
                + DUMMY_EMOJI_SUBTYPE);
        return DUMMY_EMOJI_SUBTYPE;
    }
}
