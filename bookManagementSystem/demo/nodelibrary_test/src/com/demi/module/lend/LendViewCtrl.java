package com.demi.module.lend;

import com.demi.service.BookService;
import com.demi.service.LendService;
import com.demi.service.UserService;
import com.demi.service.impl.BookServiceImpl;
import com.demi.service.impl.LendServiceImpl;
import com.demi.service.impl.UserServiceImpl;
import com.gn.App;
import com.demi.bean.Constant;
import com.demi.bean.Lend;
import com.demi.bean.User;
import com.demi.global.util.Alerts;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * ͼ�����
 *
 * @author admin
 */
public class LendViewCtrl implements Initializable {

    @FXML
    private TableView<Lend> lendTableView;
    @FXML
    private TableColumn<Lend, String> c1;
    @FXML
    private TableColumn<Lend,String> c2;
    @FXML
    private TableColumn<Lend, String> c3;
    @FXML
    private TableColumn<Lend, String> c4;
    @FXML
    private TableColumn<Lend, String> c5;
    @FXML
    private TableColumn<Lend, String> c6;
    @FXML
    private TableColumn<Lend, String> c7;

    @FXML
    private TextField lendNameField;

    @FXML
    private TextField isbnField;

    ObservableList<Lend> lends = FXCollections.observableArrayList();

    // ʵ�������ķ����
    LendService lendService = new LendServiceImpl();

    // ʵ�����û������
    UserService userService = new UserServiceImpl();

    // ʵ����ͼ������
    BookService bookService = new BookServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Lend> lendList = lendService.query(null);
        // �ж��Ƿ����ڹ黹�鼮
        lendList.forEach(lend -> {
            // �õ��黹����
            LocalDate returnDate = lend.getReturnDate();
            // ��ȡ��ǰ����
             LocalDate now = LocalDate.now();
            // �޸ĵ�ǰ���ڣ����Լ���Ч��
//            LocalDate now = LocalDate.parse("2021-09-01");

            // ����ʱ���
            Period period = Period.between(returnDate, now);

            // �鿴�û����
            User user = lend.getUser();
            BigDecimal money = user.getMoney();
            BigDecimal delay;
            if (period.getDays() >= 1) {
                /*
                    ���ڹ���
                        �������30�죬��ۿ�30Ԫ��
                        ���û�г���30�죬�򳬳�1���1Ԫ��
                        �жϵ�ǰ�û�������Ƿ����0�����С��0�����û�״̬��Ϊ���ᡣ

                 */
                // �������ɽ�
                if (period.getDays() >= 30) {
                    delay = new BigDecimal("30");
                } else
                    delay = new BigDecimal(period.getDays());
                // �ۿ�
                user.setMoney(money.subtract(delay));
                // ���黹���ڸ�Ϊ����
                lend.setReturnDate(now);
                // �ж��û����
                if (BigDecimal.ZERO.compareTo(user.getMoney()) >= 0) {
                    user.setStatus(Constant.USER_FROZEN);
                }
                // ���Ľ�������
                lend.setUser(user);

                // ˢ�½���
//                bookTableView.refresh();
//                userTableView.refresh();
                // ����service�㣬�����ļ��е�����
                userService.update(user);
                lendService.update(lend);
            }
        });


        lends.addAll(lendList);
        /*Book book = new Book(1, "javaʵս����", "����", Constant.TYPE_COMPUTER, "12-987", "XX������", Constant.STATUS_STORAGE);
        User user = new User(1, "����", "����", new BigDecimal(("100")));
        LocalDate now = LocalDate.now();
        lends.add(new Lend("1",book,user, Constant.LEND_LEND, now,now.plusDays(30)));*/

        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        //��ȡͼ������
        c2.setCellValueFactory((TableColumn.CellDataFeatures<Lend, String> p) ->
            new SimpleObjectProperty(p.getValue().getBook().getBookName())
        );
        c3.setCellValueFactory((TableColumn.CellDataFeatures<Lend, String> p) ->
                new SimpleObjectProperty(p.getValue().getBook().getIsbn())
        );
        c4.setCellValueFactory((TableColumn.CellDataFeatures<Lend, String> p) ->
                new SimpleObjectProperty(p.getValue().getUser().getName())
        );
        c5.setCellValueFactory(new PropertyValueFactory<>("lendDate"));
        c6.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        c7.setCellValueFactory(new PropertyValueFactory<>("status"));
        lendTableView.setItems(lends);
    }


    /*
        ��ѯ
     */
    @FXML
    private void lendSelect(){
        String lendName = lendNameField.getText();
        String isbn = isbnField.getText();
        boolean lendFlag = "".equals(lendName);
        boolean isbnFlag = "".equals(isbn);
        ObservableList<Lend> result = lends;
        if (lendFlag && isbnFlag) {
            return;
        }else {
//            if (!lendFlag){
//                result = lends.filtered(s -> s.getLendName().contains(lendName));
//            }
//            if (!isbnFlag) {
//                result = lends.filtered(s -> s.getIsbn().contains(isbn));
//            }
        }

        lends = new ObservableListWrapper<>(new ArrayList<>(result));
        lendTableView.setItems(lends);
    }

    /*
        ����
     */
    @FXML
    private void returnBook(){
        Lend lend = this.lendTableView.getSelectionModel().getSelectedItem();
        if (lend == null){
            Alerts.warning("δѡ��","����ѡ��Ҫ�黹���鼮");
            return;
        }
        List<Lend> lendList= lendService.returnBook(lend);
        lends = new ObservableListWrapper<>(new ArrayList<>(lendList));
        // ���½��Ĺ������
        lendTableView.setItems(lends);
//        // ����ͼ��������
//        bookTableView.refresh();
    }

    /*
        ����
     */
    @FXML
    private void renew(){
        Lend lend = this.lendTableView.getSelectionModel().getSelectedItem();
        if (lend == null){
            Alerts.warning("δѡ��","����ѡ��Ҫ������鼮");
            return;
        }
        lend.setReturnDate(LocalDate.now().plusDays(30));
    }


    /*
        ��ʼ��stage
     */
    private void initStage(Lend lend) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/com/demi/module/lend/LendHandleView.fxml"));
        StackPane target = (StackPane) loader.load();
        //Scene scene1 = App.getDecorator().getScene();
        Scene scene = new Scene(target);


        Stage stage = new Stage();//������̨��
        LendHandleViewCtrl controller = (LendHandleViewCtrl)loader.getController();

        controller.setStage(stage);
        controller.setLends(lends);
        controller.setLend(lend);
        controller.setLendTableView(lendTableView);
//        stage.setResizable(false);
        stage.setHeight(800);
        stage.setWidth(700);
        //���ô���ͼ��
        stage.getIcons().add(new Image("icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene); //������������̨��
        stage.show(); //��ʾ���ڣ�
    }

}
