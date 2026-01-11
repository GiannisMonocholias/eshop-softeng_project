package gr.softeng.team21.view.admin.createEmp;

import androidx.lifecycle.ViewModel;

public class CreateEmployeeViewModel extends ViewModel {

    private CreateEmployeePresenter presenter;

    public CreateEmployeeViewModel(){
        this.presenter = new CreateEmployeePresenter();
    }

    public CreateEmployeePresenter getPresenter(){
        return  presenter;
    }
}
