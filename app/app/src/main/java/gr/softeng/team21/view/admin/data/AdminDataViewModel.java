package gr.softeng.team21.view.admin.data;

import androidx.lifecycle.ViewModel;

public class AdminDataViewModel extends ViewModel {

    private AdminDataPresenter presenter;

    public AdminDataViewModel(){
        this.presenter = new AdminDataPresenter();
    }

    public AdminDataPresenter getPresenter(){
        return presenter;
    }
}
