import React from 'react';
import ProductList from './components/ProductList.tsx';

function App() {
  return (
    <div style={{ maxWidth: 900, margin: '0 auto', padding: 32, background: '#181a20', minHeight: '100vh' }}>
Product Management      <ProductList />
    </div>
  );
}

export default App; 