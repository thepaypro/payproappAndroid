package app.paypro.payproapp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import app.paypro.payproapp.utils.LocaleHelper;

/**
 * Created by rogerbaiget on 24/1/18.
 */

public class LanguageFragment extends Fragment {

    private ListView listview;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    public static LanguageFragment newInstance() {
        LanguageFragment fragment = new LanguageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.language_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView toolbarTitle = getActivity().findViewById(R.id.app_toolbar_title);
        toolbarTitle.setText(R.string.title_language);

        ImageButton toolbar_back_button_image = getActivity().findViewById(R.id.app_toolbar_back_button_image);
        toolbar_back_button_image.setVisibility(View.VISIBLE);

        TextView toolbar_back_button_text = getActivity().findViewById(R.id.app_toolbar_back_button_text);
        toolbar_back_button_text.setText(R.string.title_settings);
        toolbar_back_button_text.setVisibility(View.VISIBLE);

        toolbar_back_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
                ((TabActivity) getActivity()).hideVirtualKeyboard();
            }
        });

        listview = (ListView) getActivity().findViewById(R.id.language_list);

        String[] avaliableLanguages = new String[] {"English", "Español", "Français"};

        arrayList = new ArrayList<String>();
        for (int i = 0; i < avaliableLanguages.length; ++i) {
            arrayList.add(avaliableLanguages[i]);
        }

        arrayAdapter = new ArrayAdapter(getContext(),R.layout.language_list_item,arrayList);

        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        LocaleHelper.setLocale(getContext(), "en");
                        getFragmentManager().popBackStack();
                        break;
                    case 1:
                        LocaleHelper.setLocale(getContext(), "es");
                        getFragmentManager().popBackStack();
                        break;
                    case 2:
                        LocaleHelper.setLocale(getContext(), "fr");
                        getFragmentManager().popBackStack();
                        break;
                    default:
                        LocaleHelper.setLocale(getContext(), "en");
                        getFragmentManager().popBackStack();
                }
                ((TabActivity)getActivity()).onLanguageChange();
            }
        });

    }

}
