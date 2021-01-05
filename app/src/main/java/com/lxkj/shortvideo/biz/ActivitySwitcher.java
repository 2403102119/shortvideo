package com.lxkj.shortvideo.biz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.ui.activity.NaviActivity;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;

import java.util.ArrayList;

public class ActivitySwitcher {


    public static void showBigPicture(final Activity from, final String url) {
        showBigPicture(from, new ArrayList<String>() {{
            add(url);
        }}, 0);
    }

    public static void showBigPicture(final Activity from, //final ArrayList<Rect> rects,
                                      final ArrayList<String> urls, final int pos) {
//        new PictureShowDlg().setImageUrls(urls).setInitPos(pos)
//                .show(((FragmentActivity) from).getSupportFragmentManager(), "big_picture");
    }

    public static void showBigAvatar(final Activity from, final String url) {
        showBigAvatar(from, new ArrayList<String>() {{
            add(url);
        }}, 0);
    }

    public static void showBigAvatar(final Activity from, //final ArrayList<Rect> rects,
                                     final ArrayList<String> urls, final int pos) {
//        new PictureShowDlg().setImageUrls(urls).setInitPos(pos).setAvatar()
//                .show(((FragmentActivity) from).getSupportFragmentManager(), "big_picture");
    }
    public static void startFrgForResultBun(final Activity from,
                                            final Class<? extends TitleFragment> to,
                                            final int requestCode,final Bundle bundle) {
        startForResultbun(from,
                new Intent(from, NaviActivity.class).putExtra(NaviActivity.EXT_FRAGMENT, to.getName()),
                requestCode,bundle);
    }


    public static void startFragment(final Activity from,
                                     final Class<? extends TitleFragment> to) {
        final Bundle bundle = new Bundle();
        bundle.putString(NaviActivity.EXT_FRAGMENT, to.getName());
        start(from, NaviActivity.class, bundle);
    }
    public static void startForResultbun(final Activity from,
                                         final Intent intent, final int requestCode,final Bundle bundle) {
        from.startActivityForResult(intent, requestCode,bundle);
        addAnimR2L(from);
    }

    public static void startFragment(final Activity from,
                                     final Class<? extends TitleFragment> to, final Bundle bundle) {
        bundle.putString(NaviActivity.EXT_FRAGMENT, to.getName());
        start(from, NaviActivity.class, bundle);
    }
    public static void startFragment(final Context from,
                                     final Class<? extends TitleFragment> to, final Bundle bundle) {
        bundle.putString(NaviActivity.EXT_FRAGMENT, to.getName());
        start(from, NaviActivity.class, bundle);
    }


    public static void startFrgForResult(final Activity from,
                                         final Class<? extends TitleFragment> to,
                                         final int requestCode) {
        startForResult(from,
                new Intent(from, NaviActivity.class).putExtra(NaviActivity.EXT_FRAGMENT, to.getName()),
                requestCode);
    }

    public static void startFrgForResult(final Activity from,
                                         final Class<? extends TitleFragment> to,
                                         final Bundle bundle,
                                         final int requestCode) {
        bundle.putString(NaviActivity.EXT_FRAGMENT, to.getName());
        startForResult(from, new Intent(from, NaviActivity.class).putExtras(bundle), requestCode);
    }

    public static void popFragment(final Activity from,
                                   final Class<? extends TitleFragment> to) {
        popFragment(from, to, null);
    }

    public static void popFragment(Activity from, Class<? extends TitleFragment> to, Bundle bundle) {
        if (null == bundle) {
            bundle = new Bundle();
        }
        bundle.putString(NaviActivity.EXT_FRAGMENT, to.getName());
        Intent intent = new Intent(from, NaviActivity.class).putExtras(bundle);
        from.startActivity(intent);
        addAnim2T(from);
    }

    public static void popFrgForResult(final Activity from,
                                       final Class<? extends TitleFragment> to,
                                       Bundle bundle,
                                       final int requestCode) {
        if (null == bundle) {
            bundle = new Bundle();
        }
        bundle.putString(NaviActivity.EXT_FRAGMENT, to.getName());
        Intent intent = new Intent(from, NaviActivity.class).putExtras(bundle);
        from.startActivityForResult(intent, requestCode);
        addAnim2T(from);
    }

    public static void start(final Activity from,
                             final Class<? extends Activity> to, final Bundle bundle) {
        start(from, new Intent(from, to).putExtras(bundle));
    }

    public static void start(final Context from,
                             final Class<? extends Activity> to, final Bundle bundle) {
        start(from, new Intent(from, to).putExtras(bundle));
    }


    public static void start(final Activity from,
                             final Class<? extends Activity> to) {
        start(from, new Intent(from, to));
    }

    public static void start(final Activity from, final Intent intent) {
        from.startActivity(intent);
        addAnimR2L(from);
    }

    public static void start(final Context from, final Intent intent) {
        from.startActivity(intent);
    }

    public static void startForResult(final Activity from,
                                      final Intent intent, final int requestCode) {
        from.startActivityForResult(intent, requestCode);
        addAnimR2L(from);
    }



    public static void addAnimR2L(final Activity act) {
        act.overridePendingTransition(R.anim.in_from_right,
                R.anim.out_to_left);

    }

    private static void addAnim2R(final Activity act) {
        act.overridePendingTransition(0, R.anim.out_to_right);
    }

    private static void addAnim2T(final Activity act) {
        act.overridePendingTransition(R.anim.in_from_bottom, 0);
    }

    private static void addAnim2B(final Activity act) {
        act.overridePendingTransition(0, R.anim.out_to_bottom);
    }

    public static void finish(final Activity act) {
        act.finish();
        addAnim2R(act);
    }

    public static void finishDown(final Activity act) {
        act.finish();
        addAnim2B(act);
    }

    public static void goBackGround(final Activity act) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        act.startActivity(intent);
    }

    public static boolean goOutWeb(final Activity act, final String url) {
        final Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        try {
            intent.setData(Uri.parse(url));
            act.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
