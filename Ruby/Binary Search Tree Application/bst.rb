require './node'
class BST

  attr_accessor :root, :size

  def initialize
    @root = nil
  end

  def insert(value)
    if @root == nil
      @root = Node.new(value)
    else
      curr_node = @root
      previous_node = @root

      while curr_node != nil
        previous_node = curr_node
        if value.to_i < curr_node.value.to_i
          curr_node = curr_node.left_child
        else
          curr_node = curr_node.right_child
        end
      end
      if value.to_i < previous_node.value.to_i
        previous_node.left_child = Node.new(value)
      else
        previous_node.right_child = Node.new(value)
      end
    end
  end

  def in_order_traversal(node = @root)
    if(node.nil?)
    else
      print " [ " if node == @root
      in_order_traversal(node.left_child)
      print " #{node.value} "
      in_order_traversal(node.right_child)
      print " ]" if node == @root
    end
  end

  def in_order_traversal_with_lambda(node = @root, lambda_argument)

    return if node.nil?

    apply(node, lambda_argument)


  end

  def apply(node, lambda)
    if(node.left_child.nil?)
    else
      apply(node.left_child, lambda)
    end
    aBlock = eval lambda
    modified_node = aBlock.call(node.value.to_i)
    print modified_node
    print ' '
    if(node.right_child.nil?)
    else
      apply(node.right_child, lambda)
    end
  end
end
