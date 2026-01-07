package gr.softeng.team21.view.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.view.user.EditData.UserEditDataPresenter;

public class UserEditDataPresenterTest {
private  UserEditDataViewStub view;
private UserEditDataPresenter presenter;
    @Before
    public void setUp() throws Exception {
        view=new UserEditDataViewStub();
        presenter=new UserEditDataPresenter(view);
    }

    @Test
    public void selection() {
        presenter.Selection(0);
        Assert.assertEquals(1,view.getUsernameCount());
        presenter.Selection(1);
        Assert.assertEquals(1,view.getPasswordCount());
        presenter.Selection(2);
        Assert.assertEquals(1,view.getAddressCount());
        presenter.Selection(3);
        Assert.assertEquals(1,view.getEmailCount());
        presenter.Selection(4);
Assert.assertEquals(1,view.getPhoneCount());



    }
}