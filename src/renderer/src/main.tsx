import './assets/main.css'

import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import { NextUIProvider } from '@nextui-org/react'

const root = document.getElementById('root') as HTMLElement
ReactDOM.createRoot(root).render(
  <React.StrictMode>
    <NextUIProvider>
      <main className="dark">
        <App />
      </main>
    </NextUIProvider>
  </React.StrictMode>
)
