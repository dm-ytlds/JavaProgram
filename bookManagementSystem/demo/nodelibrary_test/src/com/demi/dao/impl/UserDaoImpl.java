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
 * 用户数据DAO（Data Access Objects, 数据接入层）层
 */
public class UserDaoImpl implements UserDao {
    /*
        普通查询
        从硬盘文件中读取数据
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
        // 如果出现异常，返回List对象
        return new ArrayList<>();
    }

    /**
     * 条件查询
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
        // 如果出现异常，返回List对象
        return new ArrayList<>();
    }

    /**
     * 用户添加操作
     * @param user
     */
    @Override
    public void add(User user) {
        /*
          读取当前文件内容的作用：
            (1) 为了提取到当前文件中编号的最大值，方便编号的增减；
            (2)
         */
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {

            // 读取文件中的List对象
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> users = (List<User>) ois.readObject();
            if (users != null) {
                // 拿到当前用户的最后一个编号
                int lastUID = users.get(users.size() - 1).getId();
                // 对当前传进来的用户的id进行加1操作
                user.setId(lastUID + 1);
                // 将user对象放入users集合中，然后将集合写入文件中
            } else {
                users = new ArrayList<>();
                // 由于添加时没有添加id值，所以如果文件中没有初始id值，就需要手动赋值
                user.setId(1001);
            }
            users.add(user);
            oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
            oos.writeObject(users);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败，请联系管理员！！"); // 抛给控制器
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
     * 修改操作
     * 和添加操作很像
     * @param user
     */
    @Override
    public void update(User user) {

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 将user文件中的users查出来
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> users = (List<User>)ois.readObject();
            if (users != null) {
                // users不为空
                // 从users中查出要修改数据的用户originUser
                User originUser = users.stream().filter(u -> u.getId() == user.getId()).findFirst().get();
                // 修改该用户的数据
                // 依次将要修改的数据传入找到的用户
                BeanUtil.fieldTemplate(originUser, user);
                // 将数据持久化到文件中
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
                oos.writeObject(users);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败，请联系管理员！！"); // 抛给控制器
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
     * 删除操作
     * @param id
     */
    @Override
    public void delete(int id) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 从文件中查出所有用户
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> users = (List<User>) ois.readObject();
            // stream流实现
            // 找出和该id相等的用户
            User originUser = users.stream().filter(u -> u.getId() == id).findFirst().get();
            // 用户删除
            users.remove(originUser);

            // 将剩余的数据持久化到文件中
            oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
            oos.writeObject(users);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败，请联系管理员！！"); // 抛给控制器
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
     * 冻结操作。
     *  将用户数据从文件中查出来，用stream流操作，找到需要冻结的用户，将其status改为FROAEN即可。
     *  然后将修改后的数据，重新写入文件中。记得刷新flush()。
     * @param id
     */
    @Override
    public void frozen(int id) {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            // 查数据
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> users = (List<User>)ois.readObject();
            if (users != null) {
                // stream流找出待冻结用户
                User user = users.stream().filter(u -> u.getId() == id).findFirst().get();
                // 修改该用户的状态
                user.setStatus(Constant.USER_FROZEN);

                // 重新将用户数据写入文件中
                oos = new ObjectOutputStream(new FileOutputStream(PathConstant.USER_PATH));
                oos.writeObject(users);
                oos.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("操作失败，请联系管理员！！");  // 抛给控制器
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
     * 查询出可以借书的用户
     * @return
     */
    @Override
    public List<User> selectUserToLend() {
        ObjectInputStream ois = null;

        try {
            // 首先，找出所有的用户
            ois = new ObjectInputStream(new FileInputStream(PathConstant.USER_PATH));
            List<User> userList = (List<User>) ois.readObject();
            if (userList != null) {
                // 查找用户的借阅状态isLend为false，以及用户的状态是正常的
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
