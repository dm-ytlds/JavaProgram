package com.demi.dao.impl;

import com.demi.bean.Constant;
import com.demi.bean.PathConstant;
import com.demi.bean.User;
import com.demi.dao.UserDao;
import com.demi.utils.BeanUtil;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * �û�����DAO��Data Access Objects, ���ݽ���㣩��
 */
public class UserDaoImpl implements UserDao {
    /*
        ��ͨ��ѯ
        ��Ӳ���ļ��ж�ȡ����
    */
    @Override
    public List<User> select() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH))) {
            List<User> users = (List<User>) ois.readObject();
            return users;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // ��������쳣������List����
        return new ArrayList<>();
    }

    /**
     * ������ѯ
     * @param user
     * @return
     */
    @Override
    public List<User> select(User user) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH))) {
            List<User> users = (List<User>) ois.readObject();
            if (users != null) {
                return users.stream().filter(u -> u.getId() == user.getId()).collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // ��������쳣������List����
        return new ArrayList<>();
    }

    /**
     * �û���Ӳ���
     * @param user
     */
    @Override
    public void add(User user) {
        /*
          ��ȡ��ǰ�ļ����ݵ����ã�
            (1) Ϊ����ȡ����ǰ�ļ��б�ŵ����ֵ�������ŵ�������
            (2)
         */
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {

            // ��ȡ�ļ��е�List����
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> users = (List<User>) ois.readObject();
            if (users != null) {
                // �õ���ǰ�û������һ�����
                int lastUID = users.get(users.size() - 1).getId();
                // �Ե�ǰ���������û���id���м�1����
                user.setId(lastUID + 1);
                // ��user�������users�����У�Ȼ�󽫼���д���ļ���
            } else {
                users = new ArrayList<>();
                // �������ʱû�����idֵ����������ļ���û�г�ʼidֵ������Ҫ�ֶ���ֵ
                user.setId(1001);
            }
            users.add(user);
            oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
            oos.writeObject(users);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ɾ��ʧ�ܣ�����ϵ����Ա����"); // �׸�������
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
            }catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }

    /**
     * �޸Ĳ���
     * ����Ӳ�������
     * @param user
     */
    @Override
    public void update(User user) {

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // ��user�ļ��е�users�����
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> users = (List<User>)ois.readObject();
            if (users != null) {
                // users��Ϊ��
                // ��users�в��Ҫ�޸����ݵ��û�originUser
                User originUser = users.stream().filter(u -> u.getId() == user.getId()).findFirst().get();
                // �޸ĸ��û�������
                // ���ν�Ҫ�޸ĵ����ݴ����ҵ����û�
                BeanUtil.fieldTemplate(originUser, user);
                // �����ݳ־û����ļ���
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
                oos.writeObject(users);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ɾ��ʧ�ܣ�����ϵ����Ա����"); // �׸�������
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ɾ������
     * @param id
     */
    @Override
    public void delete(int id) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // ���ļ��в�������û�
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> users = (List<User>) ois.readObject();
            // stream��ʵ��
            // �ҳ��͸�id��ȵ��û�
            User originUser = users.stream().filter(u -> u.getId() == id).findFirst().get();
            // �û�ɾ��
            users.remove(originUser);

            // ��ʣ������ݳ־û����ļ���
            oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
            oos.writeObject(users);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ɾ��ʧ�ܣ�����ϵ����Ա����"); // �׸�������
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ���������
     *  ���û����ݴ��ļ��в��������stream���������ҵ���Ҫ������û�������status��ΪFROAEN���ɡ�
     *  Ȼ���޸ĺ�����ݣ�����д���ļ��С��ǵ�ˢ��flush()��
     * @param id
     */
    @Override
    public void frozen(int id) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // ������
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> users = (List<User>)ois.readObject();
            if (users != null) {
                // stream���ҳ��������û�
                User user = users.stream().filter(u -> u.getId() == id).findFirst().get();
                // �޸ĸ��û���״̬
                user.setStatus(Constant.USER_FROZEN);

                // ���½��û�����д���ļ���
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
                oos.writeObject(users);
                oos.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("����ʧ�ܣ�����ϵ����Ա����");  // �׸�������
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (oos != null) {
                    oos.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ��ѯ�����Խ�����û�
     * @return
     */
    @Override
    public List<User> selectUserToLend() {
        ObjectInputStream ois = null;

        try {
            // ���ȣ��ҳ����е��û�
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> userList = (List<User>) ois.readObject();
            if (userList != null) {
                // �����û��Ľ���״̬isLendΪfalse���Լ��û���״̬��������
                List<User> users = userList.stream().filter(user -> Constant.USER_OK.equals(user.getStatus()) && false == user.isLend()).collect(Collectors.toList());
                return users;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}
