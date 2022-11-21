package database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    void createTable() {
        Database database = new Database(null);
        assertSame(Result.Status.OK, database.query("create table test1 (INT id, STR name)").getStatus());
        assertSame(Result.Status.OK, database.query("create table test2 (INT id, STR name)").getStatus());

        Result result = database.query("list tables");
        assertSame(Result.Status.OK, result.getStatus());
        assertSame(2, result.getRows().size());
    }

    @Test
    void email() {
        Database database = new Database(null);
        assertSame(Result.Status.OK, database.query("create table test1 (EMAIL email)").getStatus());

        assertSame(Result.Status.OK, database.query("insert into test1 (email) values(darianna697@gmail.com)").getStatus());
        assertSame(Result.Status.FAIL, database.query("insert into test1 (email) values(darianna697)").getStatus());
    }

    @Test
    void subtractOperation() {
        Database database = new Database(null);
        assertSame(Result.Status.OK, database.query("create table all_cats (INT id, STR name, FLOAT weight)").getStatus());
        assertSame(Result.Status.OK, database.query("create table black_cats (INT id, STR name)").getStatus());

        assertSame(Result.Status.OK, database.query("insert into all_cats  (id, name, weight) values(1, cat1, 2.3)").getStatus());
        assertSame(Result.Status.OK, database.query("insert into all_cats  (id, name, weight) values(3, cat2, 5.1)").getStatus());
        assertSame(Result.Status.OK, database.query("insert into all_cats  (id, name, weight) values(4, cat4, 3.0)").getStatus());
        assertSame(Result.Status.OK, database.query("insert into all_cats  (id, name, weight) values(6, cat8, 2.2)").getStatus());

        assertSame(Result.Status.OK, database.query("insert into black_cats  (id, name) values(3, cat2)").getStatus());
        assertSame(Result.Status.OK, database.query("insert into black_cats  (id, name) values(4, cat4)").getStatus());
        assertSame(Result.Status.OK, database.query("insert into black_cats  (id, name) values(6, cat15)").getStatus());

        Result result = database.query("SUBTRACT all_cats FROM black_cats");
        assertSame(Result.Status.OK, result.getStatus());
        assertSame(2, result.getRows().size());

        result = database.query("SUBTRACT black_cats FROM all_cats");
        assertSame(Result.Status.OK, result.getStatus());
        assertSame(1, result.getRows().size());
    }

}