package tk.onlinesilkstore.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdaptor extends FragmentPagerAdapter {
    public SectionsPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int posistion) {
        switch (posistion)
        {
            case 0:
                RequestFragment requestFragment=new RequestFragment();
                return requestFragment;
            case 1:
                ChatFragment chatFragment=new ChatFragment();
                return chatFragment;
            case 2:
                FriendsFragment friendsFragment=new FriendsFragment();
                return friendsFragment;
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int posistion)
    {
      switch (posistion)
      {
          case 0:
              return "Requests";
          case 1:
              return "Chats";
          case 2:
              return  "Friends";
          default:
              return null;
      }

    }
}
