
const FormImageBlock = ({src, alt, credit}) => {
  return (
    <div className='w-6/12 h-full'>
        <img src={src} alt={alt} className='w-fit' />
          <p className='text-slate-300 text-sm'>{credit.tb}<a href={credit.href} className='text-slate-400'>{credit.tl}</a>{credit.ta}</p>
    </div>
  )
}

export default FormImageBlock;