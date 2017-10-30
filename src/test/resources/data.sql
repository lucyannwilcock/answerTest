INSERT INTO T_ITEM (id, name, price) VALUES (1, 'A pre-populated item', 45.00);
INSERT INTO T_ORDER (id, paid) VALUES (1, 1);
INSERT INTO T_RECEIPT (id, ORDER_ID, payment) VALUES (1, 1, 45.00);
INSERT INTO T_LINE_ITEM (id, ITEM_ID, quantity) VALUES (1, 1, 1);