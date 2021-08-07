package com.demi.module.book;

import com.demi.service.LendService;
import com.demi.service.impl.LendServiceImpl;
import com.gn.App;
import com.demi.bean.Book;
import com.demi.bean.Constant;
import com.demi.bean.Lend;
import com.demi.bean.User;
import com.demi.module.user.UserSelectViewCtrl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;


public class BookLendViewCtrl {

    @FXML
    private TextField bookIdField;

    @FXML
    private TextField bookNameField;

    @FXML
    private TextField userIdField;

    @FXML
    private TextField userNameField;

    private Stage stage;

    //借阅的书
    private Book book;

    //借阅者
    private User user;

    // 实例化借阅管理服务
    LendService lendService = new LendServiceImpl();

    // 用户刷新页面，需要实现set和get方法
    private TableView<Book> bookTableView;

    @FXML
    private void closeView() {
        stage.close();
    }

    @FXML
    private void add() {
        // 调用借阅管理层的添加方法
        lendService.add(Integer.parseInt(userIdField.getText()), Integer.parseInt(bookIdField.getText()));

        // 修改在页面上展示的图书状态和用户借阅状态
        book.setStatus(Constant.STATUS_LEND);
        user.setLend(true);
        // 还需要刷新页面
        bookTableView.refresh();
        stage.close();
    }

    /*
        初始化借阅用户选择的stage
    */
    @FXML
    private void initSelectUserStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/com/demi/module/user/UserSelectView.fxml"));
        StackPane target = (StackPane) loader.load();
        Scene scene = new Scene(target);

        Stage stage = new Stage();//创建舞台；
        UserSelectViewCtrl controller = (UserSelectViewCtrl)loader.getController();
        controller.setStage(stage);
        controller.setBookLendViewCtrl(this);
        stage.setHeight(800);
        stage.setWidth(700);
        //设置窗口图标
        stage.getIcons().add(new Image("icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene); //将场景载入舞台；
        stage.show(); //显示窗口；
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
        bookIdField.setText(String.valueOf(book.getId()));
        bookNameField.setText(book.getBookName());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        userIdField.setText(String.valueOf(user.getId()));
        userNameField.setText(user.getName());
    }

    public TableView<Book> getBookTableView() {
        return bookTableView;
    }

    public void setBookTableView(TableView<Book> bookTableView) {
        this.bookTableView = bookTableView;
    }
}
