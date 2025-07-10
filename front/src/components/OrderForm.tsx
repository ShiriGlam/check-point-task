import React, { useState } from 'react';
import { Product, OrderDto } from '../types/Product';
import { productApi } from '../services/api';
import './OrderForm.css';

interface OrderFormProps {
  product: Product;
  onOrderComplete: () => void;
  onCancel: () => void;
}

const OrderForm: React.FC<OrderFormProps> = ({ product, onOrderComplete, onCancel }) => {
  const [quantity, setQuantity] = useState(1);
  const [error, setError] = useState<string | null>(null);
  const [isProcessing, setIsProcessing] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setIsProcessing(true);

    try {
      const orderData: OrderDto = {
        productId: product.id,
        quantity: quantity
      };

      await productApi.processOrder(orderData);
      onOrderComplete();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Error processing order');
    } finally {
      setIsProcessing(false);
    }
  };

  return (
    <div className="order-form-overlay">
      <div className="order-form">
        <h2>Place Order</h2>
        <div className="product-info">
          <h3>{product.name}</h3>
          <p>Category: {product.category}</p>
          <p>Price: ${product.price.toFixed(2)}</p>
          <p>Available Stock: {product.quantity}</p>
        </div>
        
        <form onSubmit={handleSubmit}>
          <div>
            <label>Quantity to Order:</label>
            <input
              type="number"
              value={quantity}
              onChange={(e) => setQuantity(Number(e.target.value))}
              min={1}
              max={product.quantity}
              required
            />
          </div>
          
          {error && <div className="error">{error}</div>}
          
          <div className="order-summary">
            <p>Total: ${(product.price * quantity).toFixed(2)}</p>
          </div>
          
          <div className="form-actions">
            <button type="submit" disabled={isProcessing}>
              {isProcessing ? 'Processing...' : 'Place Order'}
            </button>
            <button type="button" onClick={onCancel}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default OrderForm; 