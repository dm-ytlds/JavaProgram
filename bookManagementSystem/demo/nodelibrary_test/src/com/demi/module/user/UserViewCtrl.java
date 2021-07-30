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
 * �û�����
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

    // ͨ���鿴ObservableList�ļ̳нṹͼ�˽⵽��ObservableListҲ��һ��List����
    // û���õ����ݿ�洢���ݣ�������List�������洢����
    ObservableList<User> users = FXCollections.observableArrayList();

    // ��ʼ���û��������
    UserService userService = new UserServiceImpl();
    // ��ʼ����������Ӧ�Ĺ�ϵ
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ����service���ѯ�õ����û�����
        List<User> userList = userService.select();
        // ���ӵ�users������
        users.addAll(userList);
        /*users.add(new User(1, "����", "����", new BigDecimal(("100"))));
        users.add(new User(2, "����", "����", new BigDecimal(("100"))));
        users.add(new User(3, "����", "����", new BigDecimal(("100"))));*/
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
                Alerts.warning("δѡ��","����ѡ��Ҫɾ��������");
                return;
            }
            // ���µ��ļ���
            // �־û��ļ��е��û�
            userService.delete(user.getId());

            // �ڴ���������ʱչʾ��ɾ���ɹ����û���Ϣ��ɾ��
            this.users.remove(user);
            Alerts.success("�ɹ�", "�����ɹ�");
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.error("ʧ��","����ʧ��");
        }
    }

    @FXML
    private void chargeView() {
        try {
            User user = this.userTableView.getSelectionModel().getSelectedItem();
            if (user == null){
                Alerts.warning("δѡ��","����ѡ��Ҫ��ֵ������");
                return;
            }
            initChargeStage(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        �����û�
     */
    @FXML
    private void frozen() {
        User user = this.userTableView.getSelectionModel().getSelectedItem();
        if (user == null){
            Alerts.warning("δѡ��","����ѡ��Ҫ�޸ĵ�����");
            return;
        }
        // ����������־û����ļ���
        userService.frozen(user.getId());
        user.setStatus(Constant.USER_FROZEN);
        userTableView.refresh();
        // ���˻��Ѷ���
        if (user.getStatus() == Constant.USER_FROZEN) {
            Alerts.warning("�����ظ�����", "������ѡ����Ҫ������û�");
            return;
        }
    }

    /*
        �޸�
     */
    @FXML
    private void userEditView() {
        try {
            User user = this.userTableView.getSelectionModel().getSelectedItem();
            if (user == null){
                Alerts.warning("δѡ��","����ѡ��Ҫ�޸ĵ�����");
                return;
            }

           initStage(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        ����
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
        ��ʼ����ֵstage
     */
    private void initChargeStage(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/com/demi/module/user/UserChargeView.fxml"));
        StackPane target = (StackPane) loader.load();
        Scene scene = new Scene(target);

        Stage stage = new Stage();//������̨��
        UserChargeViewCtrl controller = (UserChargeViewCtrl)loader.getController();
        controller.setStage(stage);
        controller.setUser(user);
        controller.setUserTableView(userTableView);
        stage.setHeight(500);
        stage.setWidth(400);
        //���ô���ͼ��
        stage.getIcons().add(new Image("icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene); //������������̨��
        stage.show(); //��ʾ���ڣ�
    }

    /*
        ��ʼ��stage
     */
    private void initStage(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/com/demi/module/user/UserHandleView.fxml"));
        StackPane target = (StackPane) loader.load();
        //Scene scene1 = App.getDecorator().getScene();
        Scene scene = new Scene(target);


        Stage stage = new Stage();//������̨��
        UserHandleViewCtrl controller = (UserHandleViewCtrl)loader.getController();
        controller.setStage(stage);
        controller.setUsers(users);
        controller.setUser(user);
        controller.setUserTableView(userTableView);
//        stage.setResizable(false);
        stage.setHeight(500);
        stage.setWidth(400);
        //���ô���ͼ��
        stage.getIcons().add(new Image("icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene); //������������̨��
        stage.show(); //��ʾ���ڣ�
    }
}