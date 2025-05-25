#include<stm32f10x.h>

void gpio_config();
void sensor_config();
void usart_config();
void pwm_config();
void sys_config();
volatile int T1, T2;
volatile char c;
int rising = 0;
int control = 0;
int capture = 0;
int	distance;
uint32_t lastCommand = 0;
uint32_t current;

int main()
{
	sys_config();
	gpio_config();
	pwm_config();
	usart_config();
	while(1)
	{
		if(capture)
		{
		distance = (T2-T1)/58;
			if(distance < 10)
			{
				GPIOA->BSRR = 0x40;
				GPIOA->BSRR = 0xFFBF0000;
		GPIOB->ODR = 0;
				GPIOA->BSRR = 0x222;
		GPIOB->BSRR = 0x8000;
		lastCommand = current;
			}
			else
			{
				GPIOA->BSRR = 0x00400000;
			}
	}
		
}
}

void sys_config()
{
		__disable_irq();
		SysTick->CTRL = 0;	
		SysTick->VAL =0;
		SysTick->LOAD = 72000000-1;
		SysTick->CTRL = 7;
		__enable_irq();
}

void gpio_config(){
	RCC->APB2ENR |= 0xD;
	
	
	GPIOA->CRL = 0;
	GPIOA->CRL |= 0x3338B33;
	
	GPIOA->CRH &= 0xFFFFFF00;
	GPIOA->CRH |= 0x00000033;
	
	GPIOB->CRL &= 0x00000FFF;
	GPIOB->CRL |= 0x8B300000;
	
	GPIOB->CRH &= 0x00FFFFFF;
	GPIOB->CRH |= 0x33000000;

	
}
void pwm_config()
{
	RCC->APB1ENR |= 4;
	
	__disable_irq();
	//Tim4
	TIM4->CNT = 0; 
	TIM4->PSC = (72-1); 
	TIM4->ARR = 60000-1;
	TIM4->CCMR1 |= 0x0160; 
	TIM4->CCR1 = 10;
	TIM4->CCER = 0x11;
	TIM4->DIER |= 4;
	TIM4->CR1 |= 1;	
	NVIC_EnableIRQ(TIM4_IRQn);
	__enable_irq();
}

void TIM4_IRQHandler(void)
{

		if(!rising)
		{
			T1 = TIM4->CCR2;
			TIM4->CCER |= 0x20;
			rising = 1;
		}
		else
		{
			T2 = TIM4->CCR2;
			TIM4->CCER &= ~0x20;
			rising = 0;
			capture = 1;
		}
		TIM4->SR &= ~TIM_SR_CC2IF;
}

void usart_config()
{
	__disable_irq();
	RCC->APB1ENR |= 0x00020000;
	
	USART2->BRR = 0x139;
	USART2->CR1 |= 0x2C;
	USART2->CR1 |= 0x2000;
	NVIC_EnableIRQ(USART2_IRQn);
	__enable_irq();
}
void USART2_IRQHandler(void)
{
	c = USART2->DR;
	switch(c)
	{
	case 'f':
		GPIOA->BSRR = 0x111;
		GPIOB->BSRR = 0x4000;
		lastCommand = current;
		break;
	case 'd':
		GPIOA->BSRR = 0x222;
		GPIOB->BSRR = 0x8000;
		lastCommand = current;
		break;
	case 'l':
		GPIOA->BSRR = 0x212;
		GPIOB->BSRR = 0x4000;
	lastCommand = current;
		break;
	case 'r':
		GPIOA->BSRR = 0x121;
		GPIOB->BSRR = 0x8000;
	lastCommand = current;
		break;
}
}
void SysTick_Handler(void)
{
	current++;
	if((current - lastCommand) > 10)
	{
		GPIOA->ODR = 0;
		GPIOB->ODR = 0;
	}
}
