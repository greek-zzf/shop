CREATE TABLE SHOPPING_CART
(
    ID         BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID    BIGINT,
    GOODS_ID   BIGINT,
    SHOP_ID    BIGINT,
    NUMBER     INT,
    STATUS     VARCHAR(16),
    CREATED_AT TIMESTAMP NOT NULL DEFAULT NOW(),
    UPDATED_AT TIMESTAMP NOT NULL DEFAULT NOW()
);

INSERT INTO SHOPPING_CART(USER_ID, GOODS_ID, SHOP_ID, NUMBER, STATUS)
VALUES (1, 1, 1, 100, 'ok');
INSERT INTO SHOPPING_CART(USER_ID, GOODS_ID, SHOP_ID, NUMBER, STATUS)
VALUES (1, 4, 2, 200, 'ok');
INSERT INTO SHOPPING_CART(USER_ID, GOODS_ID, SHOP_ID, NUMBER, STATUS)
VALUES (1, 5, 2, 300, 'ok');