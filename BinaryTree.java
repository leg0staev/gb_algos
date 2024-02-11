public class BinaryTree<K, V> {
    private Node<K, V> root;

    public BinaryTree() {
    }

    // Метод для вставки ноды в красно-черное дерево
    public V insert(K key, V value) {
        // Создаем новую ноду с заданными ключом и значением
        Node<K, V> newNode = new Node<>(key, value);
        // Инициализируем переменную для хранения старого значения, если такое есть
        V oldValue = null;
        // Если дерево пустое, то делаем новую ноду корнем дерева и окрашиваем ее в
        // черный цвет
        if (root == null) {
            root = newNode;
            root.color = Node.Color.BLACK;
        } else {
            // Иначе ищем место для вставки новой ноды в дереве
            Node<K, V> current = root; // Текущая нода, с которой сравниваем новую ноду
            Node<K, V> parent = null; // Родительская нода для текущей ноды
            while (current != null) {
                // Сравниваем ключи нод
                int cmp = newNode.compareTo(current.key);
                // Если ключи равны, то заменяем значение текущей ноды на новое и возвращаем
                // старое значение
                if (cmp == 0) {
                    oldValue = current.value;
                    current.value = value;
                    return oldValue;
                }
                // Если хэш ключа новой ноды меньше, чем ключ текущей ноды, то идем в левое
                // поддерево
                else if (cmp > 0) {
                    parent = current;
                    current = current.leftChild;
                }
                // Если ключ новой ноды больше, чем ключ текущей ноды, то идем в правое
                // поддерево
                else {
                    parent = current;
                    current = current.rightChild;
                }
            }
            // Вставляем новую ноду в дерево и делаем ее родителем найденной родительской
            // ноды
            newNode.parent = parent;
            // Если ключ новой ноды меньше, чем ключ родительской ноды, то делаем новую ноду
            // левым ребенком родителя
            if (newNode.compareTo(parent.key) > 0) {
                parent.leftChild = newNode;
            }
            // Иначе делаем новую ноду правым ребенком родителя
            else {
                parent.rightChild = newNode;
            }
            // Окрашиваем новую ноду в красный цвет
            newNode.color = Node.Color.RED;
            // Вызываем метод для балансировки дерева после вставки новой ноды
            balanceAfterInsert(newNode);
        }
        // Возвращаем null, если старого значения не было
        return oldValue;
    }

    // Метод для балансировки дерева после вставки новой ноды
    // Метод для балансировки красно-черного дерева после вставки ноды
    private Node<K, V> balanceAfterInsert(Node<K, V> node) {
        // Проверяем, что нода не null
        if (node == null) {
            return null;
        }
        // Сохраняем исходную ноду для возврата
        Node<K, V> originalNode = node;
        // Пока нода не является корнем дерева и ее родитель имеет красный цвет
        while (node != root && node.parent.color == Node.Color.RED) {
            // Определяем, является ли родитель ноды левым ребенком деда ноды
            boolean left = node.parent == node.parent.parent.leftChild;
            // Определяем дядю ноды, который является братом родителя ноды
            Node<K, V> uncle = left ? node.parent.parent.rightChild : node.parent.parent.leftChild;
            // Если дядя ноды тоже имеет красный цвет
            if (uncle != null && uncle.color == Node.Color.RED) {
                // То перекрашиваем родителя и дядю ноды в черный цвет
                node.parent.color = Node.Color.BLACK;
                uncle.color = Node.Color.BLACK;
                // Перекрашиваем деда ноды в красный цвет
                node.parent.parent.color = Node.Color.RED;
                // Делаем деда ноды текущей нодой
                node = node.parent.parent;
            }
            // Иначе, если дядя ноды имеет черный цвет или отсутствует
            else {
                // Определяем, является ли нода правым ребенком родителя
                boolean right = node == node.parent.rightChild;
                // Если нода и родитель находятся по разные стороны относительно деда
                if (left != right) {
                    // То делаем родителя ноды текущей нодой
                    node = node.parent;
                    // И выполняем левый или правый поворот в зависимости от расположения нод
                    if (left) {
                        rotateLeft(node);
                    } else {
                        rotateRight(node);
                    }
                }
                // Перекрашиваем родителя ноды в черный цвет
                node.parent.color = Node.Color.BLACK;
                // Перекрашиваем деда ноды в красный цвет
                node.parent.parent.color = Node.Color.RED;
                // Выполняем правый или левый поворот в зависимости от расположения нод
                if (left) {
                    rotateRight(node.parent.parent);
                } else {
                    rotateLeft(node.parent.parent);
                }
            }
            // Проверяем, не вошли ли мы в бесконечный цикл
            if (node == originalNode) {
                // Если да, то выходим из цикла
                break;
            }
        }
        // Убеждаемся, что корень дерева имеет черный цвет
        root.color = Node.Color.BLACK;
        // Возвращаем исходную ноду
        return originalNode;
    }

    // Метод для левого поворота ноды
    private void rotateLeft(Node<K, V> node) {
        // Сохраняем правого ребенка ноды
        Node<K, V> rightChild = node.rightChild;
        // Делаем левого ребенка правого ребенка ноды правым ребенком ноды
        node.rightChild = rightChild.leftChild;
        // Если левый ребенок правого ребенка ноды существует, то делаем ноду его
        // родителем
        if (rightChild.leftChild != null) {
            rightChild.leftChild.parent = node;
        }
        // Делаем родителя ноды родителем правого ребенка ноды
        rightChild.parent = node.parent;
        // Если нода является корнем дерева, то делаем правого ребенка ноды корнем
        // дерева
        if (node.parent == null) {
            root = rightChild;
        }
        // Иначе, если нода является левым ребенком родителя, то делаем правого ребенка
        // ноды левым ребенком родителя
        else if (node == node.parent.leftChild) {
            node.parent.leftChild = rightChild;
        }
        // Иначе делаем правого ребенка ноды правым ребенком родителя
        else {
            node.parent.rightChild = rightChild;
        }
        // Делаем ноду левым ребенком правого ребенка ноды
        rightChild.leftChild = node;
        // Делаем правого ребенка ноды родителем ноды
        node.parent = rightChild;
    }

    // Метод для правого поворота ноды
    private void rotateRight(Node<K, V> node) {
        // Сохраняем левого ребенка ноды
        Node<K, V> leftChild = node.leftChild;
        // Делаем правого ребенка левого ребенка ноды левым ребенком ноды
        node.leftChild = leftChild.rightChild;
        // Если правый ребенок левого ребенка ноды существует, то делаем ноду его
        // родителем
        if (leftChild.rightChild != null) {
            leftChild.rightChild.parent = node;
        }
        // Делаем родителя ноды родителем левого ребенка ноды
        leftChild.parent = node.parent;
        // Если нода является корнем дерева, то делаем левого ребенка ноды корнем дерева
        if (node.parent == null) {
            root = leftChild;
        }
        // Иначе, если нода является правым ребенком родителя, то делаем левого ребенка
        // ноды правым ребенком родителя
        else if (node == node.parent.rightChild) {
            node.parent.rightChild = leftChild;
        }
        // Иначе, если нода является левым ребенком родителя, то делаем левого ребенка
        // ноды левым ребенком родителя
        else {
            node.parent.leftChild = leftChild;
        }
        // Делаем ноду правым ребенком левого ребенка ноды
        leftChild.rightChild = node;
        // Делаем левого ребенка ноды родителем ноды
        node.parent = leftChild;
    }

    /**
     * Node
     */
    static class Node<K, V> implements Comparable<K> {

        K key;
        V value;
        final int hash;
        Color color;
        Node<K, V> leftChild;
        Node<K, V> rightChild;
        Node<K, V> parent;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = key.hashCode();
        }

        enum Color {
            RED, BLACK
        }

        @Override
        public int compareTo(K o) {
            int thisHashCode = this.hash;
            int oHashCode = o.hashCode();

            if (thisHashCode < oHashCode) {
                return 1;
            }
            if (thisHashCode > oHashCode) {
                return -1;
            }
            return 0;
        }

        @Override
        public String toString() {
            return "Node [key=" + key + ", value=" + value + ", color=" + color + "]";
        }

    }

}
