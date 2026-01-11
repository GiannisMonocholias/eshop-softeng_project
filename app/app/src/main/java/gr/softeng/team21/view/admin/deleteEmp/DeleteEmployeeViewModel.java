package gr.softeng.team21.view.admin.deleteEmp;

import androidx.lifecycle.ViewModel;

public class DeleteEmployeeViewModel extends ViewModel {

    private DeleteEmployeePresenter presenter;

    public DeleteEmployeeViewModel(){
        this.presenter = new DeleteEmployeePresenter();
    }

    public DeleteEmployeePresenter getPresenter(){
        return presenter;
    }

}
