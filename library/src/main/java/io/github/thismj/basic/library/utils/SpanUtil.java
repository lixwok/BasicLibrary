package io.github.thismj.basic.library.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * Spannable工具类
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-08 00:29
 */

public class SpanUtil {

    public static SpannableBuilder builder(String text) {
        return new SpannableBuilder(text);
    }

    public static class SpannableBuilder {

        String span;
        String text;
        SpannableString spannableString;

        public SpannableBuilder(String text) {
            this.span = "";
            this.text = text;
            this.spannableString = new SpannableString(text);
        }

        public SpannableBuilder make(String span) {
            this.span = span;
            return this;
        }

        /**
         * {@link ForegroundColorSpan}
         */
        public SpannableBuilder foregroundColor(int color) {
            setSpan(new ForegroundColorSpan(color), findStart(), findEnd());
            return this;
        }

        /**
         * {@link ForegroundColorSpan}
         */
        public SpannableBuilder foregroundColor(String color, String span) {
            setSpan(new ForegroundColorSpan(Color.parseColor(color)), findStart(), findEnd());
            return this;
        }

        /**
         * {@link RelativeSizeSpan}
         */
        public SpannableBuilder relativeSize(float proportion, String span) {
            setSpan(new RelativeSizeSpan(proportion), findStart(), findEnd());
            return this;
        }

        /**
         * {@link AbsoluteSizeSpan}
         */
        public SpannableBuilder absoluteSize(int size, String span) {
            setSpan(new AbsoluteSizeSpan(size), findStart(), findEnd());
            return this;
        }

        /**
         * {@link UnderlineSpan}
         */
        public SpannableBuilder underline(String span) {
            setSpan(new UnderlineSpan(), findStart(), findEnd());
            return this;
        }

        /**
         * {@link StyleSpan}
         */
        public SpannableBuilder bold(String span) {
            setSpan(new StyleSpan(Typeface.BOLD), findStart(), findEnd());
            return this;
        }

        /**
         * {@link StyleSpan}
         */
        public SpannableBuilder italic(String span) {
            setSpan(new StyleSpan(Typeface.ITALIC), findStart(), findEnd());
            return this;
        }

        /**
         * {@link ClickableSpan}
         */
        public SpannableBuilder click(TextView target, final View.OnClickListener listener, String span) {
            setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    listener.onClick(widget);
                }
            }, findStart(), findEnd());

            target.setMovementMethod(LinkMovementMethod.getInstance());
            target.setFocusable(false);
            target.setClickable(false);
            target.setLongClickable(false);

            return this;
        }

        public int findStart() {
            return span.indexOf(text);
        }

        public int findEnd() {
            return span.indexOf(text) + span.length();
        }

        public SpannableString setSpan(Object what, int start, int end) {
            spannableString.setSpan(what, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        public SpannableString build() {
            return spannableString;
        }
    }
}
