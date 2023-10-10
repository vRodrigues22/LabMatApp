package com.example.ipet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ipet.R;

public class NotificationFragment extends Fragment {

    private TextView notificationTextView;
    private Button closeButton;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationTextView = rootView.findViewById(R.id.notificationTextView);
        closeButton = rootView.findViewById(R.id.closeButton);

        // Simular a exibição de uma notificação
        String notificationText = "Nova notificação recebida!";
        notificationTextView.setText(notificationText);

        // Configurar o clique do botão de fechar a notificação
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Feche a notificação ou faça alguma ação
                // Você pode até mesmo remover o fragmento daqui, se desejar
                getParentFragmentManager().popBackStack();
            }
        });

        // Retornar a view do layout configurado
        return rootView;




    }
    }

