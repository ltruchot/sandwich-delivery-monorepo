module.exports = {
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#fef5ee',
          100: '#fdead7',
          200: '#fad0ae',
          300: '#f6ad7b',
          400: '#f18145',
          500: '#ed5f20',
          600: '#de4416',
          700: '#b83014',
          800: '#922818',
          900: '#762316',
          950: '#400e09'
        },
        secondary: {
          50: '#f3f9f3',
          100: '#e4f1e2',
          200: '#cae3c7',
          300: '#a1cd9c',
          400: '#71b068',
          500: '#4f9446',
          600: '#3d7735',
          700: '#325e2c',
          800: '#2a4b26',
          900: '#233e21',
          950: '#102010'
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      animation: {
        'fade-in': 'fadeIn 0.3s ease-in-out',
        'slide-up': 'slideUp 0.3s ease-out',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { transform: 'translateY(10px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
      },
    },
  },
  plugins: [],
}