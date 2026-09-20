import { useState, useEffect } from 'react'
import { createClient } from '@supabase/supabase-js'
import './App.css'

// Utilizar variables de entorno de Vite (.env)
const supabaseUrl = import.meta.env.VITE_SUPABASE_URL
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_ANON_KEY

const supabase = createClient(supabaseUrl, supabaseAnonKey)

function App() {
  const [session, setSession] = useState(null)
  const [resultado, setResultado] = useState('')

  useEffect(() => {
    supabase.auth.getSession().then(({ data: { session } }) => setSession(session))
    
    const { data: { subscription } } = supabase.auth.onAuthStateChange((_event, session) => {
      setSession(session)
    })
    
    return () => subscription.unsubscribe()
  }, [])

  const iniciarViaje = async (idVehiculo) => {
    try {
        setResultado('Enviando solicitud...');
        // Llamada al endpoint de Spring Boot
        const response = await fetch(`http://localhost:8080/api/viajes/iniciar?idVehiculo=${idVehiculo}&usuarioId=${session.user.id}`, {
            method: 'POST'
        });
        
        const data = await response.text();
        
        if (!response.ok) {
            throw new Error(data);
        }
        
        setResultado(`Éxito: ${data}`);
    } catch (error) {
        setResultado(`Error capturado: ${error.message}`);
    }
  }

  if (!session) {
    return (
      <div style={{ padding: '2rem', textAlign: 'center', fontFamily: 'sans-serif' }}>
        <h1>UV Move - Control de Viajes</h1>
        <p>Por favor, inicia sesión para continuar.</p>
        {/* El proveedor GitHub viene activado por defecto en proyectos nuevos de Supabase para pruebas, o puedes usar signInWithOtp para email */}
        <button 
          onClick={() => supabase.auth.signInWithOAuth({ provider: 'github' })}
          style={{ padding: '10px 20px', fontSize: '16px', cursor: 'pointer' }}
        >
          Ingresar con GitHub
        </button>
      </div>
    )
  }

  return (
    <div style={{ padding: '2rem', fontFamily: 'sans-serif', maxWidth: '800px', margin: '0 auto' }}>
      <h1>UV Move - Panel de Control</h1>
      <p>Bienvenido, <strong>{session.user.email}</strong></p>
      <button 
        onClick={() => supabase.auth.signOut()}
        style={{ marginBottom: '2rem', padding: '8px 16px', backgroundColor: '#f44336', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
      >
        Cerrar Sesión
      </button>
      
      <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
        <button 
          onClick={() => iniciarViaje(1)}
          style={{ padding: '12px 24px', backgroundColor: '#4CAF50', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '16px' }}
        >
          Ejecutar Escenario 1 (Exitoso)
        </button>
        <button 
          onClick={() => iniciarViaje(2)}
          style={{ padding: '12px 24px', backgroundColor: '#ff9800', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer', fontSize: '16px' }}
        >
          Ejecutar Escenario 2 (Fallo por Batería)
        </button>
      </div>

      <div style={{ marginTop: '2rem', padding: '1rem', border: '1px solid #ccc', borderRadius: '8px', backgroundColor: '#f9f9f9' }}>
        <h3>Consola / Resultado del Backend:</h3>
        <pre style={{ whiteSpace: 'pre-wrap', wordWrap: 'break-word', color: resultado.includes('Error') ? 'red' : (resultado.includes('Éxito') ? 'green' : 'black') }}>
          {resultado || 'Esperando acción...'}
        </pre>
      </div>
    </div>
  )
}

export default App
