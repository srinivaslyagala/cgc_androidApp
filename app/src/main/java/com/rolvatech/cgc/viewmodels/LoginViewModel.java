package com.rolvatech.cgc.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

public class LoginViewModel extends BaseObservable {
    public final ObservableField<String> email = new ObservableField<>("");
    public final ObservableField<String> password = new ObservableField<>("");

}
