/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Class;
import model.Setting;
import model.Subject;
import model.User;
/**
 *
 * @author Lenovo
 */
public class ClassDBContext extends DBContext<Class> {

    public ArrayList<Class> listClassByUser(int uid) {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            String sql = "SELECT class_id,class_code,s.subject_id,s.subject_name,subject_code,trainer_id,term_id,supporter_id,c.status,description FROM swp391.class c\n"
                    + "Join swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "Where supporter_id = ? OR trainer_id = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, uid);
            stm.setInt(2, uid);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Class c = new Class();
                c.setClass_id(rs.getInt("class_id"));
                c.setClass_code(rs.getString("class_code"));
                Subject s = new Subject();
                s.setId(rs.getInt("subject_id"));
                s.setCode(rs.getString("subject_code"));
                s.setName(rs.getString("subject_name"));
                c.setSubject_id(s);
                c.setStatus(rs.getInt("status"));
                c.setDescription(rs.getString("description"));
                User supporter = new User();
                supporter.setId(rs.getInt("supporter_id"));
                c.setSupporter_id(supporter);
                classes.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return classes;
    }

    public ArrayList<Class> listClasses(int trainerId) {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT\n"
                    + "c.class_id, \n"
                    + "c.class_code, \n"
                    + "c.subject_id, \n"
                    + "s.subject_name \n"
                    + "FROM swp391.class c\n"
                    + "JOIN swp391.subject s ON c.subject_id = s.subject_id \n";
            int count = 0;
            if (trainerId != -1) {
                sql += "WHERE c.trainer_id = ? OR c.supporter_id = ?\n";
                count++;
            }

            PreparedStatement stmt = connection.prepareStatement(sql);

            if (count != 0) {
                stmt.setInt(1, trainerId);
                stmt.setInt(2, trainerId);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Class classTemp = new Class();
                classTemp.setClass_id(rs.getInt("c.class_id"));
                classTemp.setClass_code(rs.getString("c.class_code"));

                Subject s = new Subject();
                s.setId(rs.getInt("c.subject_id"));
                s.setName(rs.getString("s.subject_name"));

                classTemp.setSubject_id(s);
                classes.add(classTemp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return classes;
    }

    @Override
    public ArrayList<Class> list() {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            String sql = "SELECT c.class_id, \n"
                    + "c.class_code, \n"
                    + "c.subject_id, \n"
                    + "s.subject_code as subject_code,\n"
                    + "s.subject_name as subject_name, \n"
                    + "c.trainer_id,\n"
                    + "u1.full_name as trainer_name, \n"
                    + "c.supporter_id,\n"
                    + "u2.full_name as supporter_name, \n"
                    + "c.term_id, \n"
                    + "st.setting_title as term_title,\n"
                    + "c.status, \n"
                    + "c.description \n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id;";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Class c = new Class();
                c.setClass_id(rs.getInt("c.class_id"));
                c.setClass_code(rs.getString("c.class_code"));

                Subject s = new Subject();
                s.setId(rs.getInt("c.subject_id"));
                s.setCode(rs.getString("subject_code"));
                s.setName(rs.getString("subject_name"));

                c.setSubject_id(s);

                User u = new User();
                u.setId(rs.getInt("c.trainer_id"));
                u.setFullname(rs.getString("trainer_name"));

                c.setTrainer_id(u);

                u = new User();
                u.setId(rs.getInt("c.supporter_id"));
                u.setFullname(rs.getString("supporter_name"));

                c.setSupporter_id(u);

                Setting st = new Setting();
                st.setSetting_id(rs.getInt("c.term_id"));
                st.setSetting_title(rs.getString("term_title"));

                c.setTerm_id(st);
                c.setStatus(rs.getInt("c.status"));
                c.setDescription(rs.getString("c.description"));

                classes.add(c);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Errol!");
        }
        return classes;
    }
    
    public ArrayList<Class> listBySupporter(int id) {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            String sql = "SELECT c.class_id, \n"
                    + "c.class_code, \n"
                    + "c.subject_id, \n"
                    + "s.subject_code as subject_code,\n"
                    + "s.subject_name as subject_name, \n"
                    + "c.trainer_id,\n"
                    + "u1.full_name as trainer_name, \n"
                    + "c.supporter_id,\n"
                    + "u2.full_name as supporter_name, \n"
                    + "c.term_id, \n"
                    + "st.setting_title as term_title,\n"
                    + "c.status, \n"
                    + "c.description \n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id\n"
                    + "WHERE c.supporter_id = " + id + ";";
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Class c = new Class();
                c.setClass_id(rs.getInt("c.class_id"));
                c.setClass_code(rs.getString("c.class_code"));

                Subject s = new Subject();
                s.setId(rs.getInt("c.subject_id"));
                s.setCode(rs.getString("subject_code"));
                s.setName(rs.getString("subject_name"));

                c.setSubject_id(s);

                User u = new User();
                u.setId(rs.getInt("c.trainer_id"));
                u.setFullname(rs.getString("trainer_name"));

                c.setTrainer_id(u);

                u = new User();
                u.setId(rs.getInt("c.supporter_id"));
                u.setFullname(rs.getString("supporter_name"));

                c.setSupporter_id(u);

                Setting st = new Setting();
                st.setSetting_id(rs.getInt("c.term_id"));
                st.setSetting_title(rs.getString("term_title"));

                c.setTerm_id(st);
                c.setStatus(rs.getInt("c.status"));
                c.setDescription(rs.getString("c.description"));

                classes.add(c);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Errol!");
        }
        return classes;
    }
    
    

    @Override
    public Class get(int id) {
        try {
            String sql = "SELECT class.class_code, \n"
                    + "class.subject_id, \n"
                    + "class.trainer_id, \n"
                    + "class.supporter_id, \n"
                    + "class.term_id, \n"
                    + "class.status, \n"
                    + "class.description \n"
                    + "FROM swp391.class \n"
                    + "WHERE class.class_id = ?;";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Class c = new Class();
                c.setClass_id(id);
                c.setClass_code(rs.getString("class.class_code"));

                Subject s = new Subject();
                s.setId(rs.getInt("class.subject_id"));

                c.setSubject_id(s);

                User u = new User();
                u.setId(rs.getInt("class.trainer_id"));

                c.setTrainer_id(u);

                u = new User();
                u.setId(rs.getInt("class.supporter_id"));

                c.setSupporter_id(u);

                Setting st = new Setting();
                st.setSetting_id(rs.getInt("class.term_id"));

                c.setTerm_id(st);
                c.setStatus(rs.getInt("class.status"));
                c.setDescription(rs.getString("class.description"));
                return c;
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Errol!");
        }
        return null;
    }

    @Override
    public void add(Class model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Class model) {
        try {
            String sql = "UPDATE swp391.class \n"
                    + "SET ";

            int count = 0;

            HashMap<Integer, Object> params = new HashMap<>();

            if (model.getClass_code()!= null) {
                count++;
                sql += "class_code = ?,\n";
                params.put(count, model.getClass_code());
            }

            if (model.getSubject_id().getId() != -1) {
                count++;
                sql += "subject_id = ?,\n";
                params.put(count, model.getSubject_id().getId());
            }

            if (model.getTrainer_id().getId() != -1) {
                count++;
                sql += "trainer_id = ?,\n";
                params.put(count, model.getTrainer_id().getId());
            }

            if (model.getTrainer_id().getId() != -1) {
                count++;
                sql += "supporter_id = ?,\n";
                params.put(count, model.getSupporter_id().getId());
            }

            if (model.getTerm_id().getSetting_id() != -1) {
                count++;
                sql += "term_id = ?,\n";
                params.put(count, model.getTerm_id().getSetting_id());
            }

            if (model.getStatus()!= -1) {
                count++;
                sql += "status = ?,\n";
                params.put(count, model.getStatus());
            }

            if (model.getDescription()!= null) {
                count++;
                sql += "description = ?,\n";
                params.put(count, model.getDescription());
            }

            sql = sql.substring(0, sql.length() - 2);
            sql += " WHERE class.class_id = ?;";

            PreparedStatement stm = connection.prepareStatement(sql);

            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                Integer key = entry.getKey();
                Object val = entry.getValue();
                stm.setObject(key, val);
            }

            count++;
            stm.setInt(count, model.getClass_id());

            stm.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public int updates(Class model) {
        int st = 0;
        try {
            String sql = "UPDATE swp391.class \n"
                    + "SET ";

            int count = 0;

            HashMap<Integer, Object> params = new HashMap<>();

            if (model.getClass_code()!= null) {
                count++;
                sql += "class_code = ?,\n";
                params.put(count, model.getClass_code());
            }

            if (model.getSubject_id().getId() != -1) {
                count++;
                sql += "subject_id = ?,\n";
                params.put(count, model.getSubject_id().getId());
            }

            if (model.getTrainer_id().getId() != -1) {
                count++;
                sql += "trainer_id = ?,\n";
                params.put(count, model.getTrainer_id().getId());
            }

            if (model.getSupporter_id().getId() != -1) {
                count++;
                sql += "supporter_id = ?,\n";
                params.put(count, model.getSupporter_id().getId());
            }

            if (model.getTerm_id().getSetting_id() != -1) {
                count++;
                sql += "term_id = ?,\n";
                params.put(count, model.getTerm_id().getSetting_id());
            }

            if (model.getStatus()!= -1) {
                count++;
                sql += "status = ?,\n";
                params.put(count, model.getStatus());
            }

            if (model.getDescription()!= null) {
                count++;
                sql += "description = ?,\n";
                params.put(count, model.getDescription());
            }

            sql = sql.substring(0, sql.length() - 2);
            sql += " WHERE class.class_id = ?;";

            PreparedStatement stm = connection.prepareStatement(sql);

            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                Integer key = entry.getKey();
                Object val = entry.getValue();
                stm.setObject(key, val);
            }

            count++;
            stm.setInt(count, model.getClass_id());

            st = stm.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return st;
    }

    
    
    @Override
    public void remove(Class model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int count(String code, Integer subject, Integer term, Integer trainer, Integer supporter, Integer status) {
        try {
            String sql = "SELECT COUNT(*) as total FROM swp391.class \n"
                    + "WHERE (1=1) ";

            int count = 0;

            HashMap<Integer, Object> params = new HashMap<>();

            if (code != null && !code.trim().isEmpty()) {
                count++;
                sql += "AND class.class_code like ? \n";
                params.put(count, code);
            }

            if (subject != null) {
                count++;
                sql += "AND class.subject_id = ? \n";
                params.put(count, subject);
            }

            if (term != null) {
                count++;
                sql += "AND class.term_id = ? \n";
                params.put(count, term);
            }

            if (trainer != null) {
                count++;
                sql += "AND class.trainer_id = ? \n";
                params.put(count, trainer);
            }

            if (supporter != null) {
                count++;
                sql += "AND class.supporter_id = ? \n";
                params.put(count, supporter);
            }

            if (status != null) {
                count++;
                sql += "AND class.status = ? \n";
                params.put(count, status);
            }

            PreparedStatement stm = connection.prepareStatement(sql);

            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                Integer key = entry.getKey();
                Object val = entry.getValue();
                stm.setObject(key, val);
            }

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                return rs.getInt("total");
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Errol!");
        }
        return -1;
    }

    public ArrayList<Class> filter(int pageindex, int pagesize, String code, Integer subject, Integer term, Integer trainer, Integer supporter, Integer status) {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            String sql = "SELECT c.class_id, \n"
                    + "c.class_code, \n"
                    + "c.subject_id,\n"
                    + "s.subject_name as subject_name, \n"
                    + "c.trainer_id,\n"
                    + "u1.full_name as trainer_name, \n"
                    + "c.supporter_id,\n"
                    + "u2.full_name as supporter_name, \n"
                    + "c.term_id, \n"
                    + "st.setting_title as term_title,\n"
                    + "c.status, \n"
                    + "c.description \n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id\n"
                    + "WHERE (1=1) \n";

            int count = 0;

            HashMap<Integer, Object> params = new HashMap<>();

            if (code != null && !code.trim().isEmpty()) {
                count++;
                sql += "AND c.class_code like ? \n";
                params.put(count, code);
            }

            if (subject != null) {
                count++;
                sql += "AND c.subject_id = ? \n";
                params.put(count, subject);
            }

            if (term != null) {
                count++;
                sql += "AND c.term_id = ? \n";
                params.put(count, term);
            }

            if (trainer != null) {
                count++;
                sql += "AND c.trainer_id = ? \n";
                params.put(count, trainer);
            }

            if (supporter != null) {
                count++;
                sql += "AND c.supporter_id = ? \n";
                params.put(count, supporter);
            }

            if (status != null) {
                count++;
                sql += "AND c.status = ? \n";
                params.put(count, status);
            }

            sql += "ORDER BY c.class_id ASC \n"
                    + "LIMIT ?,?";

            PreparedStatement stm = connection.prepareStatement(sql);

            for (Map.Entry<Integer, Object> entry : params.entrySet()) {
                Integer key = entry.getKey();
                Object val = entry.getValue();
                stm.setObject(key, val);
            }

            count++;
            stm.setInt(count, (pageindex - 1) * pagesize);

            count++;
            stm.setInt(count, pagesize);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Class c = new Class();
                c.setClass_id(rs.getInt("c.class_id"));
                c.setClass_code(rs.getString("c.class_code"));

                Subject s = new Subject();
                s.setId(rs.getInt("c.subject_id"));
                s.setName(rs.getString("subject_name"));

                c.setSubject_id(s);

                User u = new User();
                u.setId(rs.getInt("c.trainer_id"));
                u.setFullname(rs.getString("trainer_name"));

                c.setTrainer_id(u);

                u = new User();
                u.setId(rs.getInt("c.supporter_id"));
                u.setFullname(rs.getString("supporter_name"));

                c.setSupporter_id(u);

                Setting st = new Setting();
                st.setSetting_id(rs.getInt("c.term_id"));
                st.setSetting_title(rs.getString("term_title"));

                c.setTerm_id(st);
                c.setStatus(rs.getInt("c.status"));
                c.setDescription(rs.getString("c.description"));

                classes.add(c);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return classes;
    }

    public ArrayList<Subject> listSubject() {
        ArrayList<Subject> subs = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT c.subject_id,\n"
                    + "s.subject_name as subject_name\n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id;";

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Subject s = new Subject();
                s.setId(rs.getInt("subject_id"));
                s.setName(rs.getString("subject_name"));

                subs.add(s);
            }
        } catch (SQLException e) {
        }
        return subs;
    }

    public Subject getSubject(int id) {
        Subject s = new Subject();
        try {
            String sql = "SELECT c.subject_id,\n"
                    + "s.subject_name as subject_name\n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id\n"
                    + "Where c.subject_id = ?;";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                s.setId(rs.getInt("subject_id"));
                s.setName(rs.getString("subject_name"));
            }
        } catch (SQLException e) {
        }
        return s;
    }

    public ArrayList<Setting> listTerm() {
        ArrayList<Setting> terms = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT c.term_id,\n"
                    + "st.setting_title as term_title\n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id;";

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Setting s = new Setting();
                s.setSetting_id(rs.getInt("term_id"));
                s.setSetting_title(rs.getString("term_title"));

                terms.add(s);
            }
        } catch (SQLException e) {
        }
        return terms;
    }

    public Setting getSetting(int id) {
        Setting s = new Setting();
        try {
            String sql = "SELECT c.term_id,\n"
                    + "st.setting_title as term_title\n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id\n"
                    + "Where c.term_id = ?;";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                s.setSetting_id(rs.getInt("term_id"));
                s.setSetting_title(rs.getString("term_title"));
            }
        } catch (SQLException e) {
        }
        return s;
    }

    public ArrayList<User> listTrainer() {
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT c.trainer_id,\n"
                    + "u1.full_name as trainer_name\n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id;";

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("trainer_id"));
                u.setFullname(rs.getString("trainer_name"));

                users.add(u);
            }
        } catch (SQLException e) {
        }
        return users;
    }

    public User getTrainer(int id) {
        User u = new User();
        try {
            String sql = "SELECT c.trainer_id,\n"
                    + "u1.full_name as trainer_name\n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id\n"
                    + "Where c.trainer_id = ?;";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                u.setId(rs.getInt("trainer_id"));
                u.setFullname(rs.getString("trainer_name"));
            }
        } catch (SQLException e) {
        }
        return u;
    }

    public ArrayList<User> listSupporter() {
        ArrayList<User> users = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT c.supporter_id,\n"
                    + "u2.full_name as supporter_name\n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id;";

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("supporter_id"));
                u.setFullname(rs.getString("supporter_name"));

                users.add(u);
            }
        } catch (SQLException e) {
        }
        return users;
    }

    public User getSupporter(int id) {
        User u = new User();
        try {
            String sql = "SELECT c.supporter_id,\n"
                    + "u2.full_name as supporter_name\n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id\n"
                    + "Where c.supporter_id = ?;";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                u.setId(rs.getInt("supporter_id"));
                u.setFullname(rs.getString("supporter_name"));
            }
        } catch (SQLException e) {
        }
        return u;
    }

    public ArrayList<Integer> listStatus() {
        ArrayList<Integer> status = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT c.status\n"
                    + "FROM swp391.class c\n"
                    + "LEFT JOIN swp391.subject s ON c.subject_id = s.subject_id\n"
                    + "LEFT JOIN swp391.user u1 ON c.trainer_id = u1.user_id\n"
                    + "LEFT JOIN swp391.user u2 ON c.supporter_id = u2.user_id\n"
                    + "LEFT JOIN swp391.setting st ON c.term_id = st.setting_id;";

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                status.add(rs.getInt("c.status"));
            }
        } catch (Exception e) {
        }

        return status;
    }

//    public static void main(String[] args) {
//        ClassDBContext cdc = new ClassDBContext();
//        ArrayList<Class> classes = cdc.list();
//        for (Class classe : classes) {
//            System.out.println(classe.toString());
//        }
//        ArrayList<Boolean> status = cdc.listStatus();
//        for (Boolean statu : status) {
//            System.out.println(statu);
//        }
//    }
}
