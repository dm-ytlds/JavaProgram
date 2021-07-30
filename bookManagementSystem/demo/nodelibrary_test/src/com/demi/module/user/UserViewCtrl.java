package com.demi.module.user;

import com.demi.service.UserService;
import com.demi.service.impl.UserServiceImpl;
import com.gn.App;
import com.demi.bean.Constant;
import com.demi.bean.User;
import com.demi.global.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 用户管理
 *
 * @author admin
 */
public class UserViewCtrl implements Initializable {

    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> c1;
    @FXML
    private TableColumn<User, String> c2;
    @FXML
    private TableColumn<User, String> c3;
    @FXML
    private TableColumn<User, String> c4;

    // 通过查看ObservableList的继承结构图了解到，ObservableList也是一个List集合
    // 没有用到数据库存储数据，所以用List集合来存储数据
    ObservableList<User> users = FXCollections.observableArrayList();

    // 初始化用户服务对象
    UserService userService = new UserServiceImpl();
    // 初始化，建立对应的关系
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 调用service层查询得到的用户数据
        List<User> userList = userService.select();
        // 添加到users对象中
        users.addAll(userList);
        /*users.add(new User(1, "张三", "正常", new BigDecimal(("100"))));
        users.add(new User(2, "李四", "正常", new BigDecimal(("100"))));
        users.add(new User(3, "王五", "正常", new BigDecimal(("100"))));*/
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
        c3.setCellValueFactory(new PropertyValueFactory<>("money"));
        c4.setCellValueFactory(new PropertyValueFactory<>("status"));
        userTableView.setItems(users);

    }

    @FXML
    private void deleteUser() {
        try {
            User user = this.userTableView.getSelectionModel().getSelectedItem();
            if (user == null){
                Alerts.warning("未选择","请先选择要删除的数据");
                return;
            }
            // 更新到文件中
            // 持久化文件中的用户
            userService.delete(user.getId());

            // 内存中用来临时展示已删除成功的用户信息的删除
            this.users.remove(user);
            Alerts.success("成功", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.error("失败","操作失败");
        }
    }

    @FXML
    private void chargeView() {
        try {
            User user = this.userTableView.getSelectionModel().getSelectedItem();
            if (user == null){
                Alerts.warning("未选择","请先选择要充值的数据");
                return;
            }
            initChargeStage(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        冻结用户
     */
    @FXML
    private void frozen() {
        User user = this.userTableView.getSelectionModel().getSelectedItem();
        if (user == null){
            Alerts.warning("未选择","请先选择要修改的数据");
            return;
        }
        // 将冻结操作持久化到文件中
        userService.frozen(user.getId());
        user.setStatus(Constant.USER_FROZEN);
        userTableView.refresh();
        // 如账户已冻结
        if (user.getStatus() == Constant.USER_FROZEN) {
            Alerts.warning("请勿重复操作", "请重新选择想要冻结的用户");
            return;
        }
    }

    /*
        修改
     */
    @FXML
    private void userEditView() {
        try {
            User user = this.userTableView.getSelectionModel().getSelectedItem();
            if (user == null){
                Alerts.warning("未选择","请先选择要修改的数据");
                return;
            }

           initStage(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        添加
     */
    @FXML
    private void userAddView() {
        try {
            initStage(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        初始化充值stage
     */
    private void initChargeStage(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/com/demi/module/user/UserChargeView.fxml"));
        StackPane target = (StackPane) loader.load();
        Scene scene = new Scene(target);

        Stage stage = new Stage();//创建舞台；
        UserChargeViewCtrl controller = (UserChargeViewCtrl)loader.getController();
        controller.setStage(stage);
        controller.setUser(user);
        controller.setUserTableView(userTableView);
        stage.setHeight(500);
        stage.setWidth(400);
        //设置窗口图标
        stage.getIcons().add(new Image("icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene); //将场景载入舞台；
        stage.show(); //显示窗口；
    }

    /*
        初始化stage
     */
    private void initStage(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/com/demi/module/user/UserHandleView.fxml"));
        StackPane target = (StackPane) loader.load();
        //Scene scene1 = App.getDecorator().getScene();
        Scene scene = new Scene(target);


        Stage stage = new Stage();//创建舞台；
        UserHandleViewCtrl controller = (UserHandleViewCtrl)loader.getController();
        controller.setStage(stage);
        controller.setUsers(users);
        controller.setUser(user);
        controller.setUserTableView(userTableView);
//        stage.setResizable(false);
        stage.setHeight(500);
        stage.setWidth(400);
        //设置窗口图标
        stage.getIcons().add(new Image("icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene); //将场景载入舞台；
        stage.show(); //显示窗口；
    }
}
