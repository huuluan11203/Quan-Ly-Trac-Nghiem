CREATE DATABASE tracnghiem;
USE tracnghiem;

-- Tạo bảng topics
CREATE TABLE topics (
    tpID INT(11) PRIMARY KEY AUTO_INCREMENT,
    tpTitle TEXT NOT NULL,
    tpParent INT(11) DEFAULT NULL,
    tpStatus TINYINT(4) NOT NULL DEFAULT 1
);

-- Tạo bảng users
CREATE TABLE users (
    userID INT(11) PRIMARY KEY AUTO_INCREMENT,
    userName VARCHAR(40) NOT NULL,
    userEmail VARCHAR(100) NOT NULL UNIQUE,
    userPassword VARCHAR(255) NOT NULL,
    userFullName VARCHAR(40) NOT NULL,
    isAdmin TINYINT(4) NOT NULL DEFAULT 0
);

-- Tạo bảng questions
CREATE TABLE questions (
    qID INT(11) PRIMARY KEY AUTO_INCREMENT,
    qContent TEXT NOT NULL,
    qPictures TEXT DEFAULT NULL,
    qTopicID INT(11) NOT NULL,
    qLevel VARCHAR(10) NOT NULL,
    qStatus TINYINT(4) NOT NULL DEFAULT 1,
    FOREIGN KEY (qTopicID) REFERENCES topics(tpID) ON DELETE CASCADE
);

-- Tạo bảng exams (trước test để tránh lỗi tham chiếu)
CREATE TABLE exams (
    testCode VARCHAR(20) NOT NULL,
    exOrder VARCHAR(5) NOT NULL,
    exCode VARCHAR(20) NOT NULL PRIMARY KEY,
    ex_quesIDs TEXT NOT NULL
);

-- Tạo bảng test
CREATE TABLE test (
    testID INT(11) PRIMARY KEY AUTO_INCREMENT,
    testCode VARCHAR(20) NOT NULL UNIQUE,
    testTitle TEXT NOT NULL,
    testTime INT(11) NOT NULL,
    tpID INT(11) NOT NULL,
    num_easy INT(11) DEFAULT 0,
    num_medium INT(11) DEFAULT 0,
    num_diff INT(11) DEFAULT 0,
    testLimit TINYINT(4) NOT NULL DEFAULT 1,
    testDate DATE NOT NULL,
    testStatus INT(11) NOT NULL DEFAULT 1,
    FOREIGN KEY (tpID) REFERENCES topics(tpID) ON DELETE CASCADE
);

-- Tạo bảng answers
CREATE TABLE answers (
    awID INT(11) PRIMARY KEY AUTO_INCREMENT,
    qID INT(11) NOT NULL,
    awContent TEXT NOT NULL,
    awPictures TEXT DEFAULT NULL,
    isRight TINYINT(4) NOT NULL DEFAULT 0,
    awStatus TINYINT(4) NOT NULL DEFAULT 1,
    FOREIGN KEY (qID) REFERENCES questions(qID) ON DELETE CASCADE
);

-- Tạo bảng result
CREATE TABLE result (
    rs_num INT(11) NOT NULL AUTO_INCREMENT,
    userID INT(11) NOT NULL,
    exCode VARCHAR(20) NOT NULL,
    rs_answers LONGTEXT NOT NULL,
    rs_mark DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    rs_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (rs_num),
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE,
    FOREIGN KEY (exCode) REFERENCES exams(exCode) ON DELETE CASCADE
);

-- Tạo bảng log
CREATE TABLE log (
    logID INT(11) PRIMARY KEY AUTO_INCREMENT,
    logContent TEXT NOT NULL,
    logUserID INT(11) NOT NULL,
    logExID VARCHAR(20) NOT NULL,
    logDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (logUserID) REFERENCES users(userID) ON DELETE CASCADE,
    FOREIGN KEY (logExID) REFERENCES exams(exCode) ON DELETE CASCADE
);
