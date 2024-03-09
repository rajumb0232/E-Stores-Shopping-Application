/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      borderWidth:{
        '0.5':'0.5px',
        '1':'1px',
        '1.5':'1.5px'
      },
      colors:{
        my_yellow: '#FFCE32',
        prussian_blue: '#1D63FF',
        standard_black: 'rgb(51, 65, 85)'
      },
      spacing: {
        '88per': '90%',
        '18': '4.4rem'
      },
      animation: {
        wiggle: 'wiggle 1s ease-in-out infinite',
      },
      keyframes: {
        wiggle: {
          '0%, 100%': { transform: 'rotate(-3deg)' },
          '50%': { transform: 'rotate(3deg)' },
        }
      },
      boxShadow:{
        'even10': '0 0 10px #e3e3e3',
        'even20': '0 0 20px #e3e3e3'
      },
      fontSize:{
        'md': '1.1rem'
      }
    },
  },
  plugins: [],
}

// font-size: 1.125rem/* 18px */;
//     line-height: 1.75rem/* 28px */;