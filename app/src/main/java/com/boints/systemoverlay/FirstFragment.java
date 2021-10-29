package com.boints.systemoverlay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.boints.systemoverlay.databinding.FragmentFirstBinding;

// add
import android.graphics.PixelFormat;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.LayoutInflater;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        binding.buttonFirst.setText("START");
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent svc = new Intent(FirstFragment.super.getActivity(), OverlayShowingService.class);
                FirstFragment.super.getActivity().startService(svc);
                FirstFragment.super.getActivity().finish();

                binding.buttonFirst.setText("STOP");
                // NavHostFragment.findNavController(FirstFragment.this)
                //       .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}